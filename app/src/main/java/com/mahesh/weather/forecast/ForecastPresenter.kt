package com.mahesh.weather.forecast

import android.util.Log
import com.mahesh.weather.app.TAG
import com.mahesh.weather.app.presenter.BasePresenterImpl
import com.mahesh.weather.forecast.adapter.ForecastAdapterModel

class ForecastPresenter : BasePresenterImpl<ForecastContract.View>(),
    ForecastContract.Presenter<ForecastContract.View>, ForecastContract.AdapterPresenter {

    override fun onResume() {
        Log.d(TAG, "onResume")
    }

    override fun getAdapterEntity(position: Int): ForecastAdapterModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAdapterEntityCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}