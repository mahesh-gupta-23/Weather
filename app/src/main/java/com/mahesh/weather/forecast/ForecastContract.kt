package com.mahesh.weather.forecast

import android.content.Intent
import com.mahesh.weather.app.presenter.BasePresenter
import com.mahesh.weather.app.presenter.BaseView
import com.mahesh.weather.forecast.adapter.ForecastAdapterModel
import com.mahesh.weather.service.models.CurrentWeather
import com.mahesh.weather.service.models.DayForecast

class ForecastContract {
    interface View : BaseView {
        fun setDate(todayDateAndTime: String)
        fun setLocation(location: String?)
        fun showSnackBar(string: String)
        fun setCurrentTemp(temp: Double?)
        fun toggleWeatherImageVisibility(show: Boolean)
        fun loadWeatherImage(weatherIcon: String?)
        fun setHumidity(humidity: Int?)
        fun notifyForecastDataChanged()
        fun toggleProgressBar(show: Boolean)
        fun showNeedLocationPermissionDialogToContinue(onOk: () -> Unit, onCancel: () -> Unit)
        fun closeApplication()
        fun showNeedLocationToBeEnabledToContinue(onOk: () -> Unit, onCancel: () -> Unit?)
        fun showErrorMessage(errorMessage: String)
    }

    interface Presenter<View : BaseView> : BasePresenter<View> {
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    }

    interface AdapterPresenter {
        fun getAdapterEntity(position: Int): ForecastAdapterModel
        fun getAdapterEntityCount(): Int
    }

    interface ModelInteractor {
        suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeather?
        suspend fun getDayForecast(lat: Double, lon: Double): List<DayForecast>?
        fun getTodayDateAndTime(): String
    }
}