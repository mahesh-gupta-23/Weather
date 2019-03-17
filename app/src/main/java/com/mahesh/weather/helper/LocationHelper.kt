package com.mahesh.weather.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.mahesh.weather.app.TAG
import com.mahesh.weather.testing.OpenForTesting
import com.mahesh.weather.util.LatLng
import com.mahesh.weather.util.REQUEST_CHECK_SETTINGS
import com.mahesh.weather.util.getLatLng
import javax.inject.Inject

@OpenForTesting
class LocationHelper @Inject constructor(
    private val activity: FragmentActivity, googleApiClient: GoogleApiClient,
    private val locationRequest: LocationRequest?, private val fusedLocationClient: FusedLocationProviderClient
) {

    private var onLocationFetched: ((latLng: LatLng) -> Unit)? = null
    private var onLocationDisabled: (() -> Unit)? = null
    private var locationCallback: LocationCallback? = null

    init {
        googleApiClient.connect()
        createLocationCallBack()
    }

    private fun createLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                returnLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(onLocationFetched: (latLng: LatLng) -> Unit, onLocationDisabled: () -> Unit) {
        this.onLocationFetched = onLocationFetched
        this.onLocationDisabled = onLocationDisabled
        fusedLocationClient.lastLocation?.addOnSuccessListener {
            if (it != null) onLocationFetched.invoke(it.getLatLng()) else checkLocationSettingAndRequestUpdate()
        }
    }

    @SuppressLint("MissingPermission")
    private fun returnLocation() {
        fusedLocationClient.lastLocation?.addOnSuccessListener {
            if (it != null) {
                onLocationFetched?.invoke(it.getLatLng())
                stopLocationUpdate()
            } else {
                checkLocationSettingAndRequestUpdate()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    private fun stopLocationUpdate() {
        fusedLocationClient.removeLocationUpdates(locationCallback!!)
    }

    private fun checkLocationSettingAndRequestUpdate() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        val locationSettingTask = LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build())
        locationSettingTask.addOnSuccessListener {
            startLocationUpdate()
        }

        locationSettingTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    /**
     * Handles the activity results
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult requestCode $requestCode")
        Log.d(TAG, "onActivityResult resultCode $resultCode")
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK ->
                    // All required changes were successfully made
                    checkLocationSettingAndRequestUpdate()
                Activity.RESULT_CANCELED
                -> {
                    onLocationDisabled?.invoke()
                }
                else -> {
                    onLocationDisabled?.invoke()
                }
            }// The user was asked to change settings, but chose not to
        }
    }
}