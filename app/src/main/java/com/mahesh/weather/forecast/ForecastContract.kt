package com.mahesh.weather.forecast

import android.content.Intent
import com.mahesh.weather.app.presenter.BasePresenter
import com.mahesh.weather.app.presenter.BaseView
import com.mahesh.weather.forecast.adapter.ForecastAdapterModel
import com.mahesh.weather.service.models.CurrentWeather

class ForecastContract {
    interface View : BaseView {

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
    }
}