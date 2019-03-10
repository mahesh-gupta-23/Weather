package com.mahesh.weather.forecast

import android.location.Location
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.mahesh.weather.app.coroutines.CoroutinesManager
import com.mahesh.weather.app.coroutines.TestCoroutinesManager
import com.mahesh.weather.helper.GeocoderHelper
import com.mahesh.weather.helper.LocationHelper
import com.mahesh.weather.helper.PermissionHelper
import com.mahesh.weather.testutils.BaseTest
import com.mahesh.weather.testutils.KotlinTestUtils.Companion.whenever
import com.mahesh.weather.testutils.Stubs
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

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
        whenever(mockPermissionHelper.isPermissionGranted(Stubs.LOCATION_PERMISSIONS)).thenReturn(false)

        //When
        mockLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        //Then
        verify(mockPermissionHelper).isPermissionGranted(Stubs.LOCATION_PERMISSIONS)
        verify(mockPermissionHelper).requestPermission(Stubs.LOCATION_PERMISSIONS)
    }

    @Test
    fun onLocationFetchItShouldDisplayOnView() {
        whenever(mockPermissionHelper.isPermissionGranted(Stubs.LOCATION_PERMISSIONS)).thenReturn(true)
        val onLocationFetchedCaptor = argumentCaptor<(location: Location) -> Unit>()
        val onLocationDisabledCaptor = argumentCaptor<() -> Unit>()

        //when
        mockLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        //then
        verify(mockLocationHelper).getLocation(onLocationFetchedCaptor.capture(), onLocationDisabledCaptor.capture())
        onLocationFetchedCaptor.firstValue.invoke(Stubs.CURRENT_LOCATION)
        verify(mockView).toggleProgressBar(false)
    }
}