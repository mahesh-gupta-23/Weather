package com.mahesh.weather.forecast

import android.Manifest
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.mahesh.weather.app.TAG
import com.mahesh.weather.app.coroutines.CoroutinesManager
import com.mahesh.weather.app.presenter.BasePresenterImpl
import com.mahesh.weather.forecast.adapter.ForecastAdapterModel
import com.mahesh.weather.helper.GeocoderHelper
import com.mahesh.weather.helper.LocationHelper
import com.mahesh.weather.helper.PermissionHelper
import com.mahesh.weather.service.models.CurrentWeather
import com.mahesh.weather.service.models.DayForecast
import com.mahesh.weather.util.AppExceptions
import com.mahesh.weather.util.CustomAddress
import com.mahesh.weather.util.FatalException
import com.mahesh.weather.util.LatLng
import java.util.*
import javax.inject.Inject

open class ForecastPresenter @Inject constructor(
    coroutinesManager: CoroutinesManager, private val locationHelper: LocationHelper,
    private val permissionHelper: PermissionHelper, private val geocoderHelper: GeocoderHelper,
    private val modelInteractor: ForecastContract.ModelInteractor
) : BasePresenterImpl<ForecastContract.View>(coroutinesManager),
    ForecastContract.Presenter<ForecastContract.View>,
    ForecastContract.AdapterPresenter,
    PermissionHelper.PermissionsListener {

    private val permissionList: MutableList<String> = mutableListOf()
    private val adapterEntityList: MutableList<ForecastAdapterModel> = mutableListOf()

    init {
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissionHelper.setPermissionListener(this)
    }

    override fun onCreate() {
        toggleProgressBar(false)
        getCurrentLocationAndDisplayWeather()
    }

    override fun getAdapterEntity(position: Int): ForecastAdapterModel = adapterEntityList[position]

    override fun getAdapterEntityCount(): Int = adapterEntityList.size

    private fun getCurrentLocationAndDisplayWeather() {
        if (permissionHelper.isPermissionGranted(permissionList)) {
            toggleProgressBar(true)
            locationHelper.getLocation({
                view()?.setDate(modelInteractor.getTodayDateAndTimeFormatted(Date()))
                showLocationData(it)
                getWeatherDataAndDisplay(it)
            }, {
                onLocationDisabled()
            })
        } else {
            permissionHelper.requestPermission(permissionList)
        }
    }

    private fun toggleProgressBar(show: Boolean) {
        view()?.toggleProgressBar(show)
    }

    private fun showLocationData(latLng: LatLng) {
        geocoderHelper.getAddress(latLng = latLng, onAddressFetched = {
            view()?.setLocation(getLocationToDisplay(it))
        }, onAddressError = {
            view()?.showSnackBar(it)
        })
    }

    private fun getLocationToDisplay(customAddress: CustomAddress): String {
        with(customAddress) {
            return when {
                !subAdminArea.isNullOrBlank() -> "$subAdminArea, $adminArea"
                !locality.isNullOrBlank() -> "$locality, $adminArea"
                else -> adminArea ?: ""
            }
        }
    }

    private fun getWeatherDataAndDisplay(latLng: LatLng) {
        launchOnUITryCatch({
            toggleProgressBar(true)
            val currentWeather =
                modelInteractor.getCurrentWeather(lat = latLng.latitude, lon = latLng.longitude)
            val dayForecastList = modelInteractor.getDayForecast(lat = latLng.latitude, lon = latLng.longitude)
            showCurrentWeatherData(currentWeather)
            createForecastAdapterEntity(dayForecastList)
            view()?.notifyForecastDataChanged()
            toggleProgressBar(false)
        }, {
            view()?.toggleProgressBar(false)
            val exception = AppExceptions.parse(it)
            if (exception is FatalException) {
                Log.d(TAG, exception.throwable.toString())
            }
            view()?.showErrorMessage(exception.errorToDisplay)
        })
    }

    private fun createForecastAdapterEntity(dayForecastList: List<DayForecast>?) {
        adapterEntityList.clear()
        dayForecastList?.forEach {
            if (adapterEntityList.size < 5) {
                adapterEntityList.add(
                    ForecastAdapterModel(
                        day = it.day,
                        date = it.date,
                        iconName = it.icon,
                        maxTemperature = it.maxTemperature,
                        minTemperature = it.minTemperature
                    )
                )
            }
        }
    }

    private fun showCurrentWeatherData(currentWeather: CurrentWeather?) {
        if (currentWeather == null)
            return
        with(currentWeather) {
            view()?.setCurrentTemp(main?.temp)
            if (weather.isNullOrEmpty()) {
                view()?.toggleWeatherImageVisibility(false)
            } else {
                view()?.toggleWeatherImageVisibility(true)
                view()?.loadWeatherImage(weather[0].icon)
            }
            view()?.setHumidity(main?.humidity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        locationHelper.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPermissionGranted() {
        getCurrentLocationAndDisplayWeather()
    }

    override fun onPermissionRejectedManyTimes(rejectedPerms: List<String>) {
        view()?.showNeedLocationPermissionDialogToContinue({
            getCurrentLocationAndDisplayWeather()
        }, {
            view()?.closeApplication()
        })
    }

    private fun onLocationDisabled() {
        view()?.showNeedLocationToBeEnabledToContinue({
            getCurrentLocationAndDisplayWeather()
        }, {
            view()?.closeApplication()
        })
    }
}
