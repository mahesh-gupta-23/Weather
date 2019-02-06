package com.mahesh.weather.app.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

interface BasePresenter<T : BaseView> : LifecycleObserver {

    fun attachView(view: T, viewLifecycle: Lifecycle)

}
