package com.mahesh.weather.service.models

import com.google.gson.annotations.SerializedName

data class ThreeHoursWeatherForecast(
    @SerializedName("dt") val dt: Long?,
    @SerializedName("main") val main: Main?,
    @SerializedName("weather") val weather: List<Weather>?,
    @SerializedName("clouds") val clouds: Clouds?,
    @SerializedName("wind") val wind: Wind?,
    @SerializedName("sys") val sys: Sys?,
    @SerializedName("dt_txt") val dtTxt: String?
)