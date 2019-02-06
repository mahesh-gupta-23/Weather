package com.mahesh.weather.forecast.di

import com.mahesh.weather.forecast.ForecastFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ForecastFragmentProvider {

    @ContributesAndroidInjector(modules = [ForecastFragmentModule::class])
    abstract fun provideForecastFragment(): ForecastFragment

}