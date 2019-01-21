package com.mahesh.weather.app.di

import com.mahesh.weather.app.WeatherApplication
import com.mahesh.weather.app.coroutines.AsyncTasksManager
import com.mahesh.weather.app.coroutines.CoroutinesManager
import com.mahesh.weather.app.coroutines.DefaultAsyncTasksManager
import com.mahesh.weather.app.coroutines.DefaultCoroutinesManager
import dagger.Provides

class AppModule constructor(private val application: WeatherApplication) {

    @Provides
    internal fun provideCoroutinesManager(): CoroutinesManager {
        return DefaultCoroutinesManager()
    }

    @Provides
    internal fun provideAsyncTasksManager(): AsyncTasksManager {
        return DefaultAsyncTasksManager()
    }
}