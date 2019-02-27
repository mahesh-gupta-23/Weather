package com.mahesh.weather.main

import android.content.Intent
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
    private var forecastFragment: ForecastFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        supportFragmentManager.findFragmentByTag(FORECAST_FRAGMENT_TAG)
                as ForecastFragment? ?: ForecastFragment().also {
            forecastFragment = it
            replaceFragmentInActivity(it, R.id.content_frame, FORECAST_FRAGMENT_TAG)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        forecastFragment?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        forecastFragment?.onActivityResult(requestCode, resultCode, data)
    }
}
