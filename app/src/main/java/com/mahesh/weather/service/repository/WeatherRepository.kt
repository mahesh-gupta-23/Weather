package com.mahesh.weather.service.repository

import com.mahesh.weather.service.models.CurrentWeather
import com.mahesh.weather.service.models.WeatherForecast

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeather?
    suspend fun getWeatherForecast(lat: Double, lon: Double): WeatherForecast?
}