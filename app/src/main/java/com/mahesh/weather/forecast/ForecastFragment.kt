package com.mahesh.weather.forecast


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mahesh.weather.R
import com.mahesh.weather.app.extensions.loadWeatherIn
import com.mahesh.weather.app.extensions.toggleVisibility
import com.mahesh.weather.databinding.FragmentForecastBinding
import com.mahesh.weather.forecast.adapter.ForecastAdapter
import com.mahesh.weather.util.REQUEST_CHECK_SETTINGS
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerFragment
import javax.inject.Inject

const val FORECAST_FRAGMENT_TAG = "FORECAST_FRAGMENT"

class ForecastFragment : DaggerFragment(), ForecastContract.View {

    private lateinit var binding: FragmentForecastBinding

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var presenter: ForecastContract.Presenter<ForecastContract.View>

    @Inject
    lateinit var picasso: Picasso

    private lateinit var adapter: ForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast, container, false)

        setupPresenter()
        adapter = ForecastAdapter(context, presenter as ForecastContract.AdapterPresenter, picasso)
        binding.rvForecast.layoutManager = GridLayoutManager(context, 5)
        binding.rvForecast.adapter = adapter
        toggleProgressBar(false)
        return binding.root
    }

    override fun setupPresenter() {
        presenter = ViewModelProviders.of(this, viewModelProvider).get(ForecastPresenter::class.java).apply {
            attachView(this@ForecastFragment, lifecycle)
        }
        lifecycle.addObserver(presenter)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        presenter.onRequestPermissionsResult(requestCode, permissions = permissions, grantResults = grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            presenter.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun setDate(todayDateAndTime: String) {
        binding.tvDate.text = todayDateAndTime
    }

    override fun setLocation(location: String?) {
        binding.tvLocation.text = location
    }

    override fun showSnackBar(string: String) {
        Snackbar.make(binding.root, string, Snackbar.LENGTH_LONG).show()
    }

    override fun setCurrentTemp(temp: Double?) {
        binding.tvCurrentTemp.text = getString(R.string.temp, temp.toString())
    }

    override fun toggleWeatherImageVisibility(show: Boolean) {
        binding.ivWeather.toggleVisibility(show)
    }

    override fun loadWeatherImage(weatherIcon: String?) {
        picasso.loadWeatherIn(weatherIcon, binding.ivWeather)
    }

    override fun setHumidity(humidity: Int?) {
        binding.tvHumidity.text = getString(R.string.relative_humidity, humidity, "%")
    }

    override fun notifyForecastDataChanged() {
        adapter.notifyDataSetChanged()
    }

    override fun toggleProgressBar(show: Boolean) {
        if (show) binding.progressBar.show() else binding.progressBar.hide()
    }

    override fun showNeedLocationPermissionDialogToContinue(onOk: () -> Unit, onCancel: () -> Unit) {
        AlertDialog.Builder(context).setTitle(getString(R.string.location_permission_required_title))
            .setMessage(getString(R.string.location_permission_required_content))
            .setPositiveButton("OK") { _, _ ->
                onOk.invoke()
            }.setNegativeButton("CANCEL") { _, _ ->
                onCancel.invoke()
            }.show()
    }

    override fun closeApplication() {
        activity?.finishAffinity()
    }

    override fun showNeedLocationToBeEnabledToContinue(onOk: () -> Unit, onCancel: () -> Unit?) {
        AlertDialog.Builder(context).setTitle(getString(R.string.location_to_be_enabled_title))
            .setMessage(getString(R.string.location_to_be_enabled_content))
            .setPositiveButton("OK") { _, _ ->
                onOk.invoke()
            }.setNegativeButton("CANCEL") { _, _ ->
                onCancel.invoke()
            }.show()
    }

    override fun showErrorMessage(errorMessage: String) {
        Snackbar.make(
            binding.root,
            getString(R.string.exception_toast_message, errorMessage),
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(R.string.ok) {
                dismiss()
            }
            show()
        }
    }
}
