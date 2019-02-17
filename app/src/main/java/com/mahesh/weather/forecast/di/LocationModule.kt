package com.mahesh.weather.forecast.di

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides

@Module
class LocationModule {

    @Provides
    internal fun provideGoogleApiClient(activity: AppCompatActivity): GoogleApiClient =
        GoogleApiClient.Builder(activity)
            .addApi(LocationServices.API).build()

    @Provides
    internal fun provideFusedLocationProviderClient(activity: AppCompatActivity): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)


    @Provides
    internal fun provideLocationRequest(): LocationRequest? =
        LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }


}