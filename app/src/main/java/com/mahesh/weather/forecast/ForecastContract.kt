package com.mahesh.weather.forecast

import com.mahesh.weather.forecast.adapter.ForecastAdapterModel

class ForecastContract {
    interface View {

    }

    interface Presenter {

        fun resume(view: View?)
        fun pause()
    }

    interface AdapterPresenter {

        fun getAdapterEntity(position: Int): ForecastAdapterModel

        fun getAdapterEntityCount(): Int

    }

    interface ModelInteractor {

    }
}