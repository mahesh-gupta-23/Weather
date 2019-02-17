package com.mahesh.weather.main

import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    internal fun provideAppCompatActivity(mainActivity: MainActivity): AppCompatActivity {
        return mainActivity
    }

}