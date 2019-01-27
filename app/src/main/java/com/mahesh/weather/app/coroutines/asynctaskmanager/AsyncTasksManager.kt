package com.mahesh.weather.app.coroutines.asynctaskmanager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred

interface AsyncTasksManager {

    suspend fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T>

    suspend fun <T> asyncAwait(block: suspend CoroutineScope.() -> T): T

    fun cancelAllAsync()

    fun cleanup()
}