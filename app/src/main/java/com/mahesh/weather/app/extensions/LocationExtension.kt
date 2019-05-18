package com.mahesh.weather.app.extensions

import android.location.Location

data class LatLng(val latitude: Double, val longitude: Double)

fun Location.getLatLng(): LatLng =
    LatLng(latitude, longitude)