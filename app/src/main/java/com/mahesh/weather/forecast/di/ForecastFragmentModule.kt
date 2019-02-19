package com.mahesh.weather.forecast.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.mahesh.weather.app.coroutines.CoroutinesManager
import com.mahesh.weather.app.coroutines.asynctaskmanager.AsyncTasksManager
import com.mahesh.weather.forecast.ForecastContract
import com.mahesh.weather.forecast.ForecastModelInteractor
import com.mahesh.weather.forecast.ForecastPresenter
import com.mahesh.weather.helper.LocationHelper
import com.mahesh.weather.helper.PermissionHelper
import com.mahesh.weather.service.repository.WeatherRepository
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
    internal fun provideForecastPresenter(
        coroutinesManager: CoroutinesManager,
        locationHelper: LocationHelper,
        permissionHelper: PermissionHelper,
        modelInteractor: ForecastContract.ModelInteractor
    ): ForecastPresenter {
        return ForecastPresenter(coroutinesManager, locationHelper, permissionHelper, modelInteractor)
    }

    @Provides
    internal fun provideForecastModelInteractor(
        asyncTasksManager: AsyncTasksManager,
        weatherRepository: WeatherRepository
    ): ForecastContract.ModelInteractor {
        return ForecastModelInteractor(asyncTasksManager, weatherRepository)
    }

    @Provides
    internal fun providesLocationHelper(
        activity: AppCompatActivity,
        googleApiClient: GoogleApiClient,
        locationRequest: LocationRequest?,
        fusedLocationClient: FusedLocationProviderClient
    ): LocationHelper {
        return LocationHelper(activity, googleApiClient, locationRequest, fusedLocationClient)
    }

    @Provides
    internal fun providesPermissionHelper(activity: AppCompatActivity): PermissionHelper {
        return PermissionHelper(activity)
    }
}