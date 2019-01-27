package com.mahesh.weather.forecast

import android.util.Log
import com.mahesh.weather.forecast.adapter.ForecastAdapterModel
import org.koin.standalone.KoinComponent

class ForecastPresenter(
    var view: ForecastContract.View?,
    val modelInteractor: ForecastContract.ModelInteractor
) : ForecastContract.Presenter, ForecastContract.AdapterPresenter, KoinComponent {

    override fun resume(view: ForecastContract.View?) {
        this.view = view
        Log.d("Forecast-Tag", "" + view)
    }

    override fun pause() {
        view = null
    }

    override fun getAdapterEntity(position: Int): ForecastAdapterModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAdapterEntityCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}