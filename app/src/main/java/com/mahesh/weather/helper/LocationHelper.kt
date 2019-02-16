package com.mahesh.weather.helper

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.mahesh.weather.app.TAG
import com.mahesh.weather.util.REQUEST_CHECK_SETTINGS

class LocationHelper(private val activity: FragmentActivity) : PermissionHelper.PermissionsListener {

    interface Callback {
        fun onLocationFetched(location: Location)
    }

    private val permissionList: MutableList<String> = mutableListOf()
    private var callback: Callback? = null
    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val permissionHelper: PermissionHelper = PermissionHelper(activity)
    private var locationCallback: LocationCallback? = null

    init {
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissionHelper.setPermissionListener(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        buildGoogleApiClient()
        createLocationCallBack()
        createLocationRequest()
    }

    private fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(activity)
            .addApi(LocationServices.API).build()
        googleApiClient!!.connect()
    }

    private fun createLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                returnLocation()
            }
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
    }

    fun getLocation(callback: Callback) {
        this.callback = callback
        if (permissionHelper.isPermissionGranted(permissionList)) {
            checkLocationSettingAndRequestUpdate()
        } else {
            permissionHelper.requestPermission(permissionList)
        }
    }

    @SuppressLint("MissingPermission")
    private fun returnLocation() {
        fusedLocationClient?.lastLocation?.addOnSuccessListener {
            if (it != null) {
                callback?.onLocationFetched(it)
                stopLocationUpdate()
            } else {
                checkLocationSettingAndRequestUpdate()
            }
        }
    }

    override fun onPermissionGranted() {
        checkLocationSettingAndRequestUpdate()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    private fun stopLocationUpdate() {
        fusedLocationClient?.removeLocationUpdates(locationCallback!!)
    }

    private fun checkLocationSettingAndRequestUpdate() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        val locationSettingTask = LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build())
        locationSettingTask.addOnSuccessListener {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
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

    override fun onPermissionRejectedManyTimes(rejectedPerms: List<String>) {

    }

    @TargetApi(Build.VERSION_CODES.M)
    fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<out String>, @NonNull grantResults: IntArray) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Handles the activity results
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult")
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK ->
                    // All required changes were successfully made
                    checkLocationSettingAndRequestUpdate()
                Activity.RESULT_CANCELED
                -> {
                }
                else -> {
                }
            }// The user was asked to change settings, but chose not to
        }
    }
}