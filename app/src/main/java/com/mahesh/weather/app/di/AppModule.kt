package com.mahesh.weather.app.di

import android.app.Application
import android.content.Context
import android.location.Geocoder
import com.mahesh.weather.app.coroutines.CoroutinesManager
import com.mahesh.weather.app.coroutines.DefaultCoroutinesManager
import com.mahesh.weather.app.coroutines.asynctaskmanager.AsyncTasksManager
import com.mahesh.weather.app.coroutines.asynctaskmanager.DefaultAsyncTasksManager
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideCoroutinesManager(): CoroutinesManager {
        return DefaultCoroutinesManager()
    }

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    internal fun provideAsyncTasksManager(): AsyncTasksManager {
        return DefaultAsyncTasksManager()
    }

    @Provides
    @Singleton
    internal fun provideGeocoder(context: Context): Geocoder {
        return Geocoder(context, Locale.getDefault())
    }
}