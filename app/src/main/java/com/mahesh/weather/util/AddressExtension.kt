package com.mahesh.weather.util

import android.location.Address

data class CustomAddress(
    val adminArea: String?,
    val subAdminArea: String?,
    val locality: String?,
    val subLocality: String?,
    val postalCode: String?,
    val countryCode: String?,
    val countryName: String?
)

fun Address.getCustomAddress(): CustomAddress =
    CustomAddress(adminArea, subAdminArea, locality, subLocality, postalCode, countryCode, countryName)
