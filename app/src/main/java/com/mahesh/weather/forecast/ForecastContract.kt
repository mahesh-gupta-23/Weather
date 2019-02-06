package com.mahesh.weather.forecast

import com.mahesh.weather.app.presenter.BasePresenter
import com.mahesh.weather.app.presenter.BaseView
import com.mahesh.weather.forecast.adapter.ForecastAdapterModel

class ForecastContract {
    interface View : BaseView {

    }

    interface Presenter<View : BaseView> : BasePresenter<View> {
    }

    interface AdapterPresenter {

        fun getAdapterEntity(position: Int): ForecastAdapterModel

        fun getAdapterEntityCount(): Int

    }

    interface ModelInteractor {

    }
}