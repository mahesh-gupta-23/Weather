package com.mahesh.weather.app.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred

interface AsyncTasksManager {

    suspend fun <T> asyncCustom(block: suspend CoroutineScope.() -> T): Deferred<T>

    suspend fun <T> asyncAwait(block: suspend CoroutineScope.() -> T): T

    fun cancelAllAsync()

    fun cleanup()
}