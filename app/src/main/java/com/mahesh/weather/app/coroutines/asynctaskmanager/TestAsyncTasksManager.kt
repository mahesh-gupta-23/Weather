package com.mahesh.weather.app.coroutines.asynctaskmanager

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.koin.standalone.KoinComponent

open class TestAsyncTasksManager : AsyncTasksManager, KoinComponent {

    override suspend fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return CompletableDeferred(runBlocking { block() })
    }

    override suspend fun <T> asyncAwait(block: suspend CoroutineScope.() -> T): T {
        return async(block).await()
    }

    override fun cancelAllAsync() {
        // Nothing to do
    }

    override fun cleanup() {
        cancelAllAsync()
    }
}