package com.mahesh.weather.forecast.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.mahesh.weather.forecast.ForecastPresenter
import com.mahesh.weather.helper.LocationHelper
import com.mahesh.weather.helper.PermissionHelper
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
        locationHelper: LocationHelper,
        permissionHelper: PermissionHelper
    ): ForecastPresenter {
        return ForecastPresenter(locationHelper, permissionHelper)
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