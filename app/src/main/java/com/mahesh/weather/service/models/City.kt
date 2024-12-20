package com.mahesh.weather.service.models

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("coord") val coord: Coord?,
    @SerializedName("country") val country: String?
)