package com.mahesh.weather.forecast


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mahesh.weather.R
import com.mahesh.weather.databinding.FragmentForecastBinding
import com.mahesh.weather.forecast.di.Properties
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.scope.ext.android.bindScope
import org.koin.android.scope.ext.android.getOrCreateScope
import org.koin.standalone.KoinComponent

class ForecastFragment : Fragment(), ForecastContract.View, KoinComponent {

    private lateinit var binding: FragmentForecastBinding
    private val presenter: ForecastContract.Presenter by inject<ForecastPresenter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast, container, false)

        bindScope(getOrCreateScope(Properties.FORECAST_SESSION))

        Log.d("Forecast-Tag", "" + this)
        Log.d("Forecast-Tag", "" + get() as ForecastFragment)
        return binding.root
    }

    override fun onResume() {
        presenter.resume(get() as ForecastFragment)
        super.onResume()
    }

}
