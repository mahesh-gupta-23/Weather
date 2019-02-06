package com.mahesh.weather.app.di

import com.mahesh.weather.BuildConfig
import com.mahesh.weather.app.coroutines.CoroutinesManager
import com.mahesh.weather.app.coroutines.DefaultCoroutinesManager
import com.mahesh.weather.app.coroutines.asynctaskmanager.AsyncTasksManager
import com.mahesh.weather.app.coroutines.asynctaskmanager.DefaultAsyncTasksManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideApiKey(): String {
        return BuildConfig.WEATHER_API_APP_ID
    }

    @Provides
    @Singleton
    internal fun provideCoroutinesManager(): CoroutinesManager {
        return DefaultCoroutinesManager()
    }


    @Provides
    @Singleton
    internal fun provideAsyncTasksManager(): AsyncTasksManager {
        return DefaultAsyncTasksManager()
    }
}