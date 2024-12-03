package com.mahesh.weather.app.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mahesh.weather.BuildConfig
import com.mahesh.weather.service.WeatherAPI
import com.mahesh.weather.service.repository.WeatherRepository
import com.mahesh.weather.service.repository.WeatherRepositoryImpl
import com.squareup.picasso.Picasso
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
    internal fun providesChuckInterceptor(context: Context): ChuckerInterceptor = ChuckerInterceptor(context)

    @Provides
    @Singleton
    internal fun provideHttpClientBuilder(chuckerInterceptor: ChuckerInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(chuckerInterceptor)
    }

    @Provides
    @Singleton
    internal fun provideHttpClient(builder: OkHttpClient.Builder): OkHttpClient = builder.build()

    @Provides
    @Singleton
    internal fun provideConverter(): Gson = Gson()

    @Provides
    @Singleton
    internal fun provideRestAdapterBuilder(client: OkHttpClient, gson: Gson): Retrofit.Builder =
        Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())

    @Provides
    @Singleton
    internal fun provideWeatherApi(builder: Retrofit.Builder): WeatherAPI =
        builder
            .baseUrl(BuildConfig.WEATHER_API_ENDPOINT)
            .build()
            .create(WeatherAPI::class.java)

    @Provides
    @Singleton
    internal fun provideWeatherRepository(repository: WeatherRepositoryImpl): WeatherRepository = repository

    @Provides
    @Singleton
    internal fun providePicasso(): Picasso = Picasso.get()
}