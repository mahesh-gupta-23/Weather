package com.mahesh.weather.app

import android.app.Application
import com.mahesh.weather.app.coroutines.di.CoroutinesModule
import com.mahesh.weather.service.di.NetworkModules
import org.koin.android.ext.android.startKoin

class WeatherApplication : Application() {

    companion object {
        private lateinit var application: WeatherApplication

        fun get(): WeatherApplication = application
    }

    override fun onCreate() {
        super.onCreate()

        application = this

        //Start Koin
        startKoin(this, weatherAppModules)
    }

    /**
     * Provides List of all the modules
     */
    private val weatherAppModules = listOf(CoroutinesModule, NetworkModules)

}