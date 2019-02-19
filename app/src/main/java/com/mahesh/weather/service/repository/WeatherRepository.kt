package com.mahesh.weather.service.repository

import com.mahesh.weather.service.models.CurrentWeather
import com.mahesh.weather.service.models.WeatherForecast

interface WeatherRepository {
    fun getCurrentWeather(lat: Double, lon: Double): CurrentWeather?
    fun getWeatherForecast(lat: Double, lon: Double): WeatherForecast?
}