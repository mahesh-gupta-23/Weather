package com.mahesh.weather.forecast

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.mahesh.weather.app.coroutines.CoroutinesManager
import com.mahesh.weather.app.coroutines.TestCoroutinesManager
import com.mahesh.weather.app.extensions.CustomAddress
import com.mahesh.weather.app.extensions.LatLng
import com.mahesh.weather.helper.GeocoderHelper
import com.mahesh.weather.helper.LocationHelper
import com.mahesh.weather.helper.PermissionHelper
import com.mahesh.weather.util.BaseTest
import com.mahesh.weather.util.KotlinTestUtils.Companion.whenever
import com.mahesh.weather.util.stubs.AddressStubs
import com.mahesh.weather.util.stubs.DataStubs
import com.mahesh.weather.util.stubs.LocationStubs
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
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
        mockLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        //Then
        verify(mockPermissionHelper).isPermissionGranted(LocationStubs.LOCATION_PERMISSIONS)
        verify(mockPermissionHelper).requestPermission(LocationStubs.LOCATION_PERMISSIONS)
    }

    @Test
    fun onPermissionRejectedManyTimes_itShouldShowDialogToRequestPermissionOrCloseApplication() {
        val onOk = argumentCaptor<() -> Unit>()
        val onCancel = argumentCaptor<() -> Unit>()

        //when
        //Setting isPermissionGranted to true initially as when onCreate is invoked the presenter tries to check for location and fetch data
        whenever(mockPermissionHelper.isPermissionGranted(LocationStubs.LOCATION_PERMISSIONS)).thenReturn(true)
        mockLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        //Setting is permission granted to false as we now need to test
        whenever(mockPermissionHelper.isPermissionGranted(LocationStubs.LOCATION_PERMISSIONS)).thenReturn(false)
        forecastPresenter.onPermissionRejectedManyTimes(listOf())

        verify(mockView).showNeedLocationPermissionDialogToContinue(onOk.capture(), onCancel.capture())
        onOk.firstValue.invoke()
        verify(mockPermissionHelper).requestPermission(LocationStubs.LOCATION_PERMISSIONS)
        onCancel.firstValue.invoke()
        verify(mockView).closeApplication()
    }

    @Test
    fun onActivityResult_itShouldBePassedToLocationHelperForProcessing() {
        //when
        forecastPresenter.onActivityResult(0, 0, null)

        //then
        verify(mockLocationHelper).onActivityResult(0, 0, null)
    }

    @Test
    fun onLocationDisabled_itShouldShowDialogToEnableOrCloseApplication() {
        whenever(mockPermissionHelper.isPermissionGranted(LocationStubs.LOCATION_PERMISSIONS)).thenReturn(true)
        val onLocationFetchedCaptor = argumentCaptor<(latLng: LatLng) -> Unit>()
        val onLocationDisabledCaptor = argumentCaptor<() -> Unit>()
        val onOk = argumentCaptor<() -> Unit>()
        val onCancel = argumentCaptor<() -> Unit>()

        //when
        mockLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        verify(mockLocationHelper).getLocation(onLocationFetchedCaptor.capture(), onLocationDisabledCaptor.capture())
        onLocationDisabledCaptor.firstValue.invoke()

        //then
        verify(mockView).showNeedLocationToBeEnabledToContinue(onOk.capture(), onCancel.capture())
        onCancel.firstValue.invoke()
        verify(mockView).closeApplication()
    }

    @Test
    fun onLocationPermissionGrantedAfterRejection_itShouldFetchCurrentLocation() {
        whenever(mockPermissionHelper.isPermissionGranted(LocationStubs.LOCATION_PERMISSIONS)).thenReturn(true)
        val onLocationFetchedCaptor = argumentCaptor<(latLng: LatLng) -> Unit>()
        val onLocationDisabledCaptor = argumentCaptor<() -> Unit>()

        forecastPresenter.onPermissionGranted()
        verify(mockLocationHelper).getLocation(onLocationFetchedCaptor.capture(), onLocationDisabledCaptor.capture())
    }

    @Test
    fun onLocationFetch_itShouldDisplayAddressOnView() {
        whenever(mockPermissionHelper.isPermissionGranted(LocationStubs.LOCATION_PERMISSIONS)).thenReturn(true)
        val onLocationFetchedCaptor = argumentCaptor<(latLng: LatLng) -> Unit>()
        val onLocationDisabledCaptor = argumentCaptor<() -> Unit>()
        val onAddressFetchedCaptor = argumentCaptor<(customAddress: CustomAddress) -> Unit>()
        val onAddressErrorCaptor = argumentCaptor<(error: String) -> Unit>()
        val mockDate: Date = mock()

        //when
        mockLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        //then
        verify(mockLocationHelper).getLocation(onLocationFetchedCaptor.capture(), onLocationDisabledCaptor.capture())
        onLocationFetchedCaptor.firstValue.invoke(LocationStubs.CURRENT_LAT_LNG)
        verify(mockView, times(2)).toggleProgressBar(false)
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

    @Test
    fun onAddressError_itShouldShowAddressNotFoundToast() {
        whenever(mockPermissionHelper.isPermissionGranted(LocationStubs.LOCATION_PERMISSIONS)).thenReturn(true)
        val onLocationFetchedCaptor = argumentCaptor<(latLng: LatLng) -> Unit>()
        val onLocationDisabledCaptor = argumentCaptor<() -> Unit>()
        val onAddressFetchedCaptor = argumentCaptor<(customAddress: CustomAddress) -> Unit>()
        val onAddressErrorCaptor = argumentCaptor<(error: String) -> Unit>()

        //when
        mockLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        //then
        verify(mockLocationHelper).getLocation(onLocationFetchedCaptor.capture(), onLocationDisabledCaptor.capture())
        onLocationFetchedCaptor.firstValue.invoke(LocationStubs.CURRENT_LAT_LNG)
        verify(mockGeocoderHelper).getAddress(
            retryCount = eq(0),
            latLng = eq(LocationStubs.CURRENT_LAT_LNG),
            onAddressFetched = onAddressFetchedCaptor.capture(),
            onAddressError = onAddressErrorCaptor.capture()
        )
        with(LocationStubs.ON_ADDRESS_ERROR) {
            onAddressErrorCaptor.firstValue.invoke(this)
            verify(mockView).showErrorMessage(this)
        }
    }

    @Test
    fun onLocationFetch_itShouldGetCurrentWeatherForThatLocationAndDisplayWeatherDataOnView() {
        whenever(mockPermissionHelper.isPermissionGranted(LocationStubs.LOCATION_PERMISSIONS)).thenReturn(true)
        runBlocking {
            whenever(
                mockModelInteractor.getCurrentWeather(
                    LocationStubs.CURRENT_LAT_LNG.latitude,
                    LocationStubs.CURRENT_LAT_LNG.longitude
                )
            ).thenReturn(DataStubs.STUB_CURRENT_WEATHER)
        }

        val onLocationFetchedCaptor = argumentCaptor<(latLng: LatLng) -> Unit>()
        val onLocationDisabledCaptor = argumentCaptor<() -> Unit>()

        //when
        mockLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        //then
        verify(mockLocationHelper).getLocation(onLocationFetchedCaptor.capture(), onLocationDisabledCaptor.capture())
        onLocationFetchedCaptor.firstValue.invoke(LocationStubs.CURRENT_LAT_LNG)
        runBlocking {
            verify(mockModelInteractor).getCurrentWeather(
                LocationStubs.CURRENT_LAT_LNG.latitude,
                LocationStubs.CURRENT_LAT_LNG.longitude
            )
        }
        verify(mockView, times(2)).toggleProgressBar(false)
        with(DataStubs.STUB_CURRENT_WEATHER) {
            verify(mockView).setCurrentTemp(main?.temp)
            verify(mockView).toggleWeatherImageVisibility(true)
            verify(mockView).loadWeatherImage(weather!![0].icon)
            verify(mockView).setHumidity(main?.humidity)
        }
    }

    @Test
    fun onLocationFetch_itShouldGetDayForecastForThatLocationAndDisplayDataOnView() {
        whenever(mockPermissionHelper.isPermissionGranted(LocationStubs.LOCATION_PERMISSIONS)).thenReturn(true)
        runBlocking {
            whenever(
                mockModelInteractor.getDayForecast(
                    LocationStubs.CURRENT_LAT_LNG.latitude,
                    LocationStubs.CURRENT_LAT_LNG.longitude
                )
            ).thenReturn(DataStubs.STUB_DAY_FORECAST)
        }

        val onLocationFetchedCaptor = argumentCaptor<(latLng: LatLng) -> Unit>()
        val onLocationDisabledCaptor = argumentCaptor<() -> Unit>()

        //when
        mockLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        //then
        verify(mockLocationHelper).getLocation(onLocationFetchedCaptor.capture(), onLocationDisabledCaptor.capture())
        onLocationFetchedCaptor.firstValue.invoke(LocationStubs.CURRENT_LAT_LNG)
        runBlocking {
            verify(mockModelInteractor).getDayForecast(
                LocationStubs.CURRENT_LAT_LNG.latitude,
                LocationStubs.CURRENT_LAT_LNG.longitude
            )
        }
        verify(mockView, times(2)).toggleProgressBar(false)
        assertEquals(DataStubs.ADAPTER_ENTITY, forecastPresenter.getAdapterEntity(0))
        verify(mockView).notifyForecastDataChanged()
    }
}