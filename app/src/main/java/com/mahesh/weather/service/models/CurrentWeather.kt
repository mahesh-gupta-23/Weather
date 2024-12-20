package com.mahesh.weather.service.models

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("coord") val coord: Coord?,
    @SerializedName("weather") val weather: List<Weather>?,
    @SerializedName("base") val base: String?,
    @SerializedName("main") val main: Main?,
    @SerializedName("wind") val wind: Wind?,
    @SerializedName("clouds") val clouds: Clouds?,
    @SerializedName("rain") val rain: Rain?,
    @SerializedName("dt") val dt: Int?,
    @SerializedName("sys") val sys: Sys?,
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("cod") val cod: Int?
)