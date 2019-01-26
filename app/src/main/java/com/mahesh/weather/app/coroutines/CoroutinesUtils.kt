package com.mahesh.weather.app.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

interface CoroutinesUtils {
    companion object {
        suspend fun tryCatch(
            tryBlock: suspend CoroutineScope.() -> Unit,
            catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
            handleCancellationExceptionManually: Boolean = false
        ) {
            coroutineScope {
                try {
                    tryBlock()
                } catch (e: Throwable) {
                    if (e !is CancellationException || handleCancellationExceptionManually) {
                        catchBlock(e)
                    } else {
                        throw e
                    }
                }
            }
        }

        suspend fun tryCatchFinally(
            tryBlock: suspend CoroutineScope.() -> Unit,
            catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
            finallyBlock: suspend CoroutineScope.() -> Unit,
            handleCancellationExceptionManually: Boolean = false
        ) {
            coroutineScope {
                var caughtThrowable: Throwable? = null
                try {
                    tryBlock()
                } catch (e: Throwable) {
                    if (e !is CancellationException || handleCancellationExceptionManually) {
                        catchBlock(e)
                    } else {
                        caughtThrowable = e
                    }
                } finally {
                    if (caughtThrowable is CancellationException && !handleCancellationExceptionManually) {
                        throw caughtThrowable
                    } else {
                        finallyBlock()
                    }
                }
            }
        }

        suspend fun tryFinally(
            tryBlock: suspend CoroutineScope.() -> Unit,
            finallyBlock: suspend CoroutineScope.() -> Unit,
            suppressCancellationException: Boolean = false
        ) {
            coroutineScope {
                var caughtThrowable: Throwable? = null
                try {
                    tryBlock()
                } catch (e: CancellationException) {
                    if (!suppressCancellationException) {
                        caughtThrowable = e
                    }
                } finally {
                    if (caughtThrowable is CancellationException && !suppressCancellationException) {
                        throw caughtThrowable
                    } else {
                        finallyBlock()
                    }
                }
            }
        }
    }
}