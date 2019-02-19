package com.mahesh.weather.service

import com.mahesh.weather.service.models.CurrentWeather
import com.mahesh.weather.service.models.WeatherForecast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    // Docs: https://openweathermap.org/current
    // Example: http://api.openweathermap.org/data/2.5/weather?lat=19.075983&lon=72.877655&units=metric&appid=b707c85fb1ca3d79f0e93158c156c56a
    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): Call<CurrentWeather>

    // Docs: https://openweathermap.org/forecast5
    // Example: http://api.openweathermap.org/data/2.5/forecast?lat=19.075983&lon=72.877655&units=metric&appid=b707c85fb1ca3d79f0e93158c156c56a
    @GET("data/2.5/forecast")
    fun getWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): Call<WeatherForecast>
}