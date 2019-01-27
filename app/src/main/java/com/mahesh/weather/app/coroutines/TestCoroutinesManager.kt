package com.mahesh.weather.app.coroutines

import com.mahesh.weather.app.coroutines.CoroutinesUtils.Companion.tryCatch
import com.mahesh.weather.app.coroutines.CoroutinesUtils.Companion.tryCatchFinally
import com.mahesh.weather.app.coroutines.CoroutinesUtils.Companion.tryFinally
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.koin.standalone.KoinComponent

open class TestCoroutinesManager : CoroutinesManager, KoinComponent {

    override fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        runBlocking { block() }
    }

    override fun launchOnUITryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        handleCancellationExceptionManually: Boolean
    ) {
        launchOnUI { tryCatch(tryBlock, catchBlock, handleCancellationExceptionManually) }
    }

    override fun launchOnUITryCatchFinally(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean
    ) {
        launchOnUI { tryCatchFinally(tryBlock, catchBlock, finallyBlock, handleCancellationExceptionManually) }
    }

    override fun launchOnUITryFinally(
        tryBlock: suspend CoroutineScope.() -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        suppressCancellationException: Boolean
    ) {
        launchOnUI { tryFinally(tryBlock, finallyBlock, suppressCancellationException) }
    }

    override fun cancelAllCoroutines() {
        // Nothing to do
    }
}