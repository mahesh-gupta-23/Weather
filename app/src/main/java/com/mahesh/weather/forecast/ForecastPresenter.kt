package com.mahesh.weather.forecast

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.mahesh.weather.app.TAG
import com.mahesh.weather.app.coroutines.CoroutinesManager
import com.mahesh.weather.app.presenter.BasePresenterImpl
import com.mahesh.weather.forecast.adapter.ForecastAdapterModel
import com.mahesh.weather.helper.LocationHelper
import com.mahesh.weather.helper.PermissionHelper
import com.mahesh.weather.service.models.CurrentWeather
import javax.inject.Inject

class ForecastPresenter @Inject constructor(
    coroutinesManager: CoroutinesManager,
    private val locationHelper: LocationHelper,
    private val permissionHelper: PermissionHelper,
    private val modelInteractor: ForecastContract.ModelInteractor
) :
    BasePresenterImpl<ForecastContract.View>(coroutinesManager),
    ForecastContract.Presenter<ForecastContract.View>,
    ForecastContract.AdapterPresenter,
    PermissionHelper.PermissionsListener {

    private val permissionList: MutableList<String> = mutableListOf()

    init {
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissionHelper.setPermissionListener(this)
    }

    override fun onCreate() {
        getLocation()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
    }

    override fun getAdapterEntity(position: Int): ForecastAdapterModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAdapterEntityCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getLocation() {
        if (permissionHelper.isPermissionGranted(permissionList)) {
            locationHelper.getLocation {
                getWeatherDataAndDisplay(location = it)
            }
        } else {
            permissionHelper.requestPermission(permissionList)
        }
    }

    private fun getWeatherDataAndDisplay(location: Location) {
        launchOnUITryCatch({
            val currentWeather = modelInteractor.getCurrentWeather(lat = location.latitude, lon = location.longitude)
            showCurrentWeatherData(currentWeather)
        }, {
            Log.d(TAG, "exception $it")
        })
    }

    private fun showCurrentWeatherData(currentWeather: CurrentWeather?) {
        Log.d(TAG, "currentWeather $currentWeather")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        locationHelper.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPermissionGranted() {
        getLocation()
    }

    override fun onPermissionRejectedManyTimes(rejectedPerms: List<String>) {
        //TODO : Show dialog on view to allow permission
    }

}