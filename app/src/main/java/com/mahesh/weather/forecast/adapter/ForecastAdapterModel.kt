package com.mahesh.weather.forecast.adapter

data class ForecastAdapterModel(
    val day: String,
    val date: String,
    val iconName: String?,
    val maxTemperature: Double?,
    val minTemperature: Double?
)
