package com.mahesh.weather.app.di

import com.mahesh.weather.forecast.di.ForecastFragmentProvider
import com.mahesh.weather.main.MainActivity
import com.mahesh.weather.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(
        modules = [
            MainActivityModule::class,
            ForecastFragmentProvider::class
        ]
    )
    abstract fun bindMainActivity(): MainActivity
}
