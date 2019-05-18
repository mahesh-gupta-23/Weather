package com.mahesh.weather.utils

import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.mahesh.weather.app.extensions.LatLng
import com.mahesh.weather.helper.LocationHelper
import com.mahesh.weather.util.KotlinTestUtils.Companion.whenever
import com.mahesh.weather.util.stubs.LocationStubs
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocationHelperTest {

    private val googleApiClient: GoogleApiClient = mock(GoogleApiClient::class.java)
    private val fusedLocationClient: FusedLocationProviderClient = mock(FusedLocationProviderClient::class.java)
    private val activity: FragmentActivity = mock(FragmentActivity::class.java)

    private val locationHelper = LocationHelper(activity, googleApiClient, null, fusedLocationClient)

    @Test
    fun initialTest() {
        val onLocationFetched: (latLng: LatLng) -> Unit = {}
        val onLocationDisabled: () -> Unit = {}
        whenever(fusedLocationClient.lastLocation).thenReturn(LocationStubs.getTaskLocation())

        locationHelper.getLocation(onLocationFetched, onLocationDisabled)

//        verify(onLocationFetched).to(LocationStubs.CURRENT_LAT_LNG)
    }

}