package com.mahesh.weather.helper

import android.Manifest
import android.content.Context
import android.location.Location

interface Callback {

    fun onLocationFetched(): Location

}

class LocationHelper(private val context: Context, private val permissionHelper: PermissionHelper) {

    private val permissionList: MutableList<String> = mutableListOf()

    init {
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    fun getLocation(callback: Callback) {
        if (permissionHelper.isPermissionGranted(permissionList)) {

        } else {
            permissionHelper.requestPermission(permissionList)
        }
    }
}