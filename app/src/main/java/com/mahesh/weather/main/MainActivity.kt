package com.mahesh.weather.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mahesh.weather.R
import com.mahesh.weather.databinding.ActivityMainBinding
import com.mahesh.weather.forecast.FORECAST_FRAGMENT_TAG
import com.mahesh.weather.forecast.ForecastFragment
import com.mahesh.weather.util.replaceFragmentInActivity
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        supportFragmentManager.findFragmentByTag(FORECAST_FRAGMENT_TAG)
                as ForecastFragment? ?: ForecastFragment().also {
            replaceFragmentInActivity(it, R.id.content_frame, FORECAST_FRAGMENT_TAG)
        }
    }

}
