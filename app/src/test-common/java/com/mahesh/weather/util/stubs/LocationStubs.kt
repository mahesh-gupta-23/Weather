package com.mahesh.weather.util.stubs

import android.Manifest
import android.location.Location
import android.location.LocationManager
import com.mahesh.weather.util.LatLng

class LocationStubs {
    companion object {
        const val LATITUDE = 19.075983
        const val LONGITUDE = 72.877655
        const val ON_ADDRESS_ERROR = "No Address Found"

        @JvmField
        val LOCATION_PERMISSIONS: List<String> =
            listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        @JvmField
        val CURRENT_LOCATION: Location = with(Location(LocationManager.GPS_PROVIDER)) {
            latitude = LATITUDE
            longitude = LONGITUDE
            time = System.currentTimeMillis()
            return@with this
        }

        @JvmField
        val CURRENT_LAT_LNG: LatLng = LatLng(LATITUDE, LONGITUDE)
    }
}