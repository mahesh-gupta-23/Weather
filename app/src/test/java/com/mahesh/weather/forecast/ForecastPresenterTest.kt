package com.mahesh.weather.forecast

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.mahesh.weather.app.coroutines.CoroutinesManager
import com.mahesh.weather.app.coroutines.TestCoroutinesManager
import com.mahesh.weather.helper.GeocoderHelper
import com.mahesh.weather.helper.LocationHelper
import com.mahesh.weather.helper.PermissionHelper
import com.mahesh.weather.util.CustomAddress
import com.mahesh.weather.util.LatLng
import com.mahesh.weather.utils.BaseTest
import com.mahesh.weather.utils.KotlinTestUtils.Companion.whenever
import com.mahesh.weather.utils.stubs.AddressStubs
import com.mahesh.weather.utils.stubs.LocationStubs
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class ForecastPresenterTest : BaseTest() {

    @Spy
    private val coroutinesManager: CoroutinesManager = TestCoroutinesManager()
    private val mockView: ForecastContract.View = mock()
    private val mockLocationHelper: LocationHelper = mock()
    private val mockPermissionHelper: PermissionHelper = mock()
    private val mockGeocoderHelper: GeocoderHelper = mock()
    private val mockModelInteractor: ForecastContract.ModelInteractor = mock()
    private val mockLifecycle = LifecycleRegistry(mock(LifecycleOwner::class.java))

    private lateinit var forecastPresenter: ForecastPresenter

    @Before
    fun before() {
        forecastPresenter = ForecastPresenter(
            coroutinesManager,
            mockLocationHelper,
            mockPermissionHelper,
            mockGeocoderHelper,
            mockModelInteractor
        )
        forecastPresenter.attachView(mockView, mockLifecycle)
    }

    @Test
    fun onLocationPermissionNotPresent_requestPermission() {
        whenever(mockPermissionHelper.isPermissionGranted(LocationStubs.LOCATION_PERMISSIONS)).thenReturn(false)

        //When
        mockLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        //Then
        verify(mockPermissionHelper).isPermissionGranted(LocationStubs.LOCATION_PERMISSIONS)
        verify(mockPermissionHelper).requestPermission(LocationStubs.LOCATION_PERMISSIONS)
    }

    @Test
    fun onLocationFetchItShouldDisplayOnView() {
        whenever(mockPermissionHelper.isPermissionGranted(LocationStubs.LOCATION_PERMISSIONS)).thenReturn(true)
        val onLocationFetchedCaptor = argumentCaptor<(latLng: LatLng) -> Unit>()
        val onLocationDisabledCaptor = argumentCaptor<() -> Unit>()
        val onAddressFetchedCaptor = argumentCaptor<(customAddress: CustomAddress) -> Unit>()
        val onAddressErrorCaptor = argumentCaptor<(error: String) -> Unit>()
        val mockDate: Date = mock()

        //when
        mockLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        //then
        verify(mockLocationHelper).getLocation(onLocationFetchedCaptor.capture(), onLocationDisabledCaptor.capture())
        onLocationFetchedCaptor.firstValue.invoke(LocationStubs.CURRENT_LAT_LNG)
        verify(mockView).toggleProgressBar(false)
        verify(mockView).setDate(mockModelInteractor.getTodayDateAndTimeFormatted(mockDate))
        verify(mockGeocoderHelper).getAddress(
            retryCount = eq(0),
            latLng = eq(LocationStubs.CURRENT_LAT_LNG),
            onAddressFetched = onAddressFetchedCaptor.capture(),
            onAddressError = onAddressErrorCaptor.capture()
        )
        with(AddressStubs.CURRENT_CUSTOM_ADDRESS) {
            onAddressFetchedCaptor.firstValue.invoke(this)
            verify(mockView).setLocation("$subAdminArea, $adminArea")
        }
    }
}