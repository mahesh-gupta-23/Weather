package com.mahesh.weather.service

import com.mahesh.weather.BuildConfig
import okhttp3.OkHttpClient
import org.koin.standalone.KoinComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Network : KoinComponent {

    private fun getHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    private fun getRestAdapterBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .client(getHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
    }

    fun getWeatherApi(): WeatherAPI {
        return getRestAdapterBuilder()
            .baseUrl(BuildConfig.WEATHER_API_ENDPOINT)
            .build()
            .create(WeatherAPI::class.java)
    }
}