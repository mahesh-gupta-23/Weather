package com.mahesh.weather.app.di

import android.content.Context
import com.google.gson.Gson
import com.mahesh.weather.BuildConfig
import com.mahesh.weather.service.WeatherAPI
import com.mahesh.weather.service.repository.WeatherRepository
import com.mahesh.weather.service.repository.WeatherRepositoryImpl
import com.readystatesoftware.chuck.ChuckInterceptor
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
    internal fun providesChuckInterceptor(context: Context): ChuckInterceptor = ChuckInterceptor(context)

    @Provides
    @Singleton
    internal fun provideHttpClientBuilder(chuckInterceptor: ChuckInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(chuckInterceptor)
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