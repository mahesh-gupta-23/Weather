package com.mahesh.weather.app

import android.app.Application
import com.mahesh.weather.app.di.AppComponent

class WeatherApplication: Application() {

    companion object {
        private lateinit var application: WeatherApplication

        fun  get(): WeatherApplication = application
    }

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        application = this


    }
}