package com.mahesh.weather.service.models

data class DayForecast(
    val day: String,
    val date: String,
    val icon: String?,
    val maxTemperature: Double?,
    val minTemperature: Double?
)