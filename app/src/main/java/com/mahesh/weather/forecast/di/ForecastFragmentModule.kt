package com.mahesh.weather.forecast.di

import androidx.lifecycle.ViewModelProvider
import com.mahesh.weather.forecast.ForecastPresenter
import com.mahesh.weather.util.ViewModelProviderFactory
import dagger.Module
import dagger.Provides

@Module
class ForecastFragmentModule {

    @Provides
    internal fun forecastViewModelProvider(forecastPresenter: ForecastPresenter):
            ViewModelProvider.Factory {
        return ViewModelProviderFactory(forecastPresenter)
    }

    @Provides
    internal fun provideForecastPresenter(): ForecastPresenter {
        return ForecastPresenter()
    }
}