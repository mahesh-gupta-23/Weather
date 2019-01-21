package com.mahesh.weather.app.di

import com.google.gson.Gson
import com.mahesh.weather.BuildConfig
import com.mahesh.weather.app.di.scopes.PerApplication
import com.mahesh.weather.service.WeatherAPI
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @Provides
    @PerApplication
    internal fun provideHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()

    @Provides
    @PerApplication
    internal fun provideHttpClient(builder: OkHttpClient.Builder): OkHttpClient = builder.build()

    @Provides
    @PerApplication
    internal fun provideConverter(): Gson = Gson()

    @Provides
    @PerApplication
    internal fun provideRestAdapterBuilder(client: OkHttpClient, gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Provides
    @PerApplication
    internal fun provideWeatherApi(builder: Retrofit.Builder): WeatherAPI {
        return builder
            .baseUrl(BuildConfig.WEATHER_API_ENDPOINT)
            .build()
            .create(WeatherAPI::class.java)
    }
}