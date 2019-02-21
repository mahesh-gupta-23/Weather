package com.mahesh.weather.service.models

data class DayForecast(
    val dayName: String,
    val description: String,
    val minTemperature: Double?,
    val maxTemperature: Double?,
    val icon: String?
)