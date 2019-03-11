package com.mahesh.weather.util

import android.location.Location

data class LatLng(val latitude: Double, val longitude: Double)

fun Location.getLatLng(): LatLng = LatLng(latitude, longitude)