package com.mahesh.weather.service.repository

import com.mahesh.weather.BuildConfig
import com.mahesh.weather.service.WeatherAPI
import com.mahesh.weather.service.models.CurrentWeather
import com.mahesh.weather.service.models.WeatherForecast
import javax.inject.Inject

class WeatherRepositoryImpl
@Inject constructor(private val weatherApi: WeatherAPI) : WeatherRepository {

    companion object {
        private const val UNITS = "metric"
    }

    override suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeather? {
        return weatherApi
            .getCurrentWeatherAsync(lat, lon, UNITS, BuildConfig.WEATHER_API_APP_ID)
            .await()
    }

    override suspend fun getWeatherForecast(lat: Double, lon: Double): WeatherForecast? {
        return weatherApi
            .getWeatherForecastAsync(lat, lon, UNITS, BuildConfig.WEATHER_API_APP_ID)
            .await()
    }
}