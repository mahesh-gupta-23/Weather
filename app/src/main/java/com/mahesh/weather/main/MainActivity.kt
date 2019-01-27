package com.mahesh.weather.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mahesh.weather.R
import com.mahesh.weather.databinding.ActivityMainBinding
import com.mahesh.weather.forecast.ForecastFragment
import com.mahesh.weather.forecast.di.Properties
import com.mahesh.weather.util.replaceFragmentInActivity
import org.koin.android.ext.android.inject
import org.koin.android.scope.ext.android.bindScope
import org.koin.android.scope.ext.android.getOrCreateScope

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val forecastFragment: ForecastFragment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bindScope(getOrCreateScope(Properties.MAIN_ACTIVITY_SESSION))

        supportFragmentManager.findFragmentById(R.id.content_frame)
                as ForecastFragment? ?: forecastFragment.also {
            replaceFragmentInActivity(it, R.id.content_frame)
        }
    }

}
