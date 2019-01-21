package com.mahesh.weather.app.di

import com.mahesh.weather.app.WeatherApplication
import dagger.Component
import com.mahesh.weather.app.di.scopes.PerApplication
import com.mahesh.weather.service.WeatherAPI

@PerApplication
@Component(modules = [(AppModule::class), (NetworkModule::class)])
interface AppComponent {
    fun inject(application: WeatherApplication)
}