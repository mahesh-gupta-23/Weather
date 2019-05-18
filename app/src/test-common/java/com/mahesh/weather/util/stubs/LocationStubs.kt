package com.mahesh.weather.util.stubs

import android.Manifest
import android.app.Activity
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.mahesh.weather.app.extensions.LatLng
import java.util.concurrent.Executor

object LocationStubs {

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
    val CURRENT_LAT_LNG: LatLng =
        LatLng(LATITUDE, LONGITUDE)

    fun getTaskLocation(): Task<Location>? {
        return object : Task<Location>() {
            override fun isComplete(): Boolean = true

            override fun getException(): Exception? = null

            override fun addOnFailureListener(p0: OnFailureListener): Task<Location> = this

            override fun addOnFailureListener(p0: Executor, p1: OnFailureListener): Task<Location> = this

            override fun addOnFailureListener(p0: Activity, p1: OnFailureListener): Task<Location> = this

            override fun getResult(): Location? = LocationStubs.CURRENT_LOCATION

            override fun <X : Throwable?> getResult(p0: Class<X>): Location? = LocationStubs.CURRENT_LOCATION

            override fun addOnSuccessListener(p0: OnSuccessListener<in Location>): Task<Location> = this

            override fun addOnSuccessListener(p0: Executor, p1: OnSuccessListener<in Location>): Task<Location> = this

            override fun addOnSuccessListener(p0: Activity, p1: OnSuccessListener<in Location>): Task<Location> = this

            override fun isSuccessful(): Boolean = true

            override fun isCanceled(): Boolean = false
        }
    }
}
