package com.mahesh.weather.app.coroutines

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking

open class TestAsyncTasksManager : AsyncTasksManager {

    override suspend fun <T> asyncCustom(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return CompletableDeferred(runBlocking { block() })
    }

    override suspend fun <T> asyncAwait(block: suspend CoroutineScope.() -> T): T {
        return asyncCustom(block).await()
    }

    override fun cancelAllAsync() {
        // Nothing to do
    }

    override fun cleanup() {
        cancelAllAsync()
    }
}