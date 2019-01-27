package com.mahesh.weather.app.coroutines.di

import com.mahesh.weather.app.coroutines.*
import com.mahesh.weather.app.coroutines.asynctaskmanager.AsyncTasksManager
import com.mahesh.weather.app.coroutines.asynctaskmanager.DefaultAsyncTasksManager
import com.mahesh.weather.app.coroutines.asynctaskmanager.TestAsyncTasksManager
import org.koin.dsl.module.module


val CoroutinesModule = module(createOnStart = true) {

    single(name = Properties.DEV) { DefaultAsyncTasksManager() as AsyncTasksManager }
    single(name = Properties.TEST) { TestAsyncTasksManager() as AsyncTasksManager }

    single(name = Properties.DEV) { DefaultCoroutinesManager() as CoroutinesManager }
    single(name = Properties.TEST) { TestCoroutinesManager() as CoroutinesManager }
}

object Properties {
    const val DEV = "DEV"
    const val TEST = "TEST"
}