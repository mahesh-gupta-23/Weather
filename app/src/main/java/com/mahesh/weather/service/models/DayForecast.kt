package com.mahesh.weather.service.models

data class DayForecast(
    val day: String,
    val date: String,
    val description: String,
    val minTemperature: Double?,
    val maxTemperature: Double?,
    val icon: String?
)