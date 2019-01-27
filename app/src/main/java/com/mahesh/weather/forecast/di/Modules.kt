package com.mahesh.weather.forecast.di

import com.mahesh.weather.forecast.ForecastContract
import com.mahesh.weather.forecast.ForecastFragment
import com.mahesh.weather.forecast.ForecastModelInteractor
import com.mahesh.weather.forecast.ForecastPresenter
import com.mahesh.weather.forecast.adapter.ForecastAdapter
import org.koin.dsl.module.module

val ForecastModule = module {
    scope(Properties.MAIN_SESSION) { ForecastFragment() }
    factory { ForecastAdapter(get(), get()) }
    factory { ForecastModelInteractor() as ForecastContract.ModelInteractor }
    scope(Properties.FORECAST_SESSION) { ForecastPresenter(get() as ForecastFragment, get()) }
}

object Properties {
    const val MAIN_SESSION = "MAIN_SESSION"
    const val FORECAST_SESSION = "FORECAST_SESSION"
}