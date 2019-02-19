package com.mahesh.weather.app.di

import com.google.gson.Gson
import com.mahesh.weather.BuildConfig
import com.mahesh.weather.service.WeatherAPI
import com.mahesh.weather.service.repository.WeatherRepository
import com.mahesh.weather.service.repository.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun provideHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()

    @Provides
    @Singleton
    internal fun provideHttpClient(builder: OkHttpClient.Builder): OkHttpClient = builder.build()

    @Provides
    @Singleton
    internal fun provideConverter(): Gson = Gson()

    @Provides
    @Singleton
    internal fun provideRestAdapterBuilder(client: OkHttpClient, gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Provides
    @Singleton
    internal fun provideWeatherApi(builder: Retrofit.Builder): WeatherAPI {
        return builder
            .baseUrl(BuildConfig.WEATHER_API_ENDPOINT)
            .build()
            .create(WeatherAPI::class.java)
    }

    @Provides
    @Singleton
    internal fun provideWeatherRepository(repository: WeatherRepositoryImpl): WeatherRepository {
        return repository
    }

}