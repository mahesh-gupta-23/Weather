package com.mahesh.weather.util

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class AppException(val throwable: Throwable, val errorToDisplay: String) : RuntimeException(throwable)
sealed class NetworkException(throwable: Throwable, errorToDisplay: String) : AppException(throwable, errorToDisplay)
sealed class FatalException(throwable: Throwable, errorToDisplay: String) : NetworkException(throwable, errorToDisplay)
sealed class NonFatalException(throwable: Throwable, errorToDisplay: String) :
    NetworkException(throwable, errorToDisplay)

class NoNetworkException(throwable: Throwable) :
    NonFatalException(throwable, "Check your internet connection and retry")

class ServerException(throwable: Throwable) : FatalException(
    throwable,
    "We are facing a lot of request currently so our servers are busy kindly retry after some time"
)

class InternalApiError(throwable: Throwable) :
    FatalException(throwable, "Sorry we encountered an error. We have logged it and will fix it soon")

class Unknown(throwable: Throwable) :
    FatalException(throwable, "That wasn't expected, we have logged it and will fix it soon")

class AppExceptions {
    companion object {
        @JvmStatic
        fun parse(throwable: Throwable): AppException {
            return when (throwable) {
                is IOException, is SocketTimeoutException -> NoNetworkException(throwable)
                is UnknownHostException -> ServerException(throwable)
                is HttpException -> {
                    return when (throwable.code()) {
                        400, in 404..428 -> InternalApiError(throwable)
                        in 429..599 -> ServerException(throwable)
                        else -> Unknown(throwable)
                    }
                }
                else -> Unknown(throwable)
            }
        }
    }
}