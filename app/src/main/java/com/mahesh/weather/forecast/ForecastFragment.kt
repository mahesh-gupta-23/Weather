package com.mahesh.weather.forecast


import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mahesh.weather.R
import com.mahesh.weather.app.TAG
import com.mahesh.weather.databinding.FragmentForecastBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

const val FORECAST_FRAGMENT_TAG = "FORECAST_FRAGMENT"

class ForecastFragment : DaggerFragment(), ForecastContract.View {

    private lateinit var binding: FragmentForecastBinding

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var presenter: ForecastContract.Presenter<ForecastContract.View>

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast, container, false)

        setupPresenter()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

        getLocation()

        return binding.root
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) !==
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            Log.d(TAG, "" + location)
        }
    }

    override fun setupPresenter() {
        presenter = ViewModelProviders.of(this, viewModelProvider).get(ForecastPresenter::class.java)
        presenter.attachView(this, lifecycle)
        lifecycle.addObserver(presenter)
    }

}
