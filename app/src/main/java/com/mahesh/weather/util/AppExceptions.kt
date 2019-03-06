package com.mahesh.weather.util

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class AppException(val throwable: Throwable, val errorToDisplay: String) : RuntimeException(throwable)
sealed class NetworkException(throwable: Throwable, errorToDisplay: String) : AppException(throwable, errorToDisplay)
class NoNetworkException(throwable: Throwable) : NetworkException(throwable, "Check your internet connection")
class ServerException(throwable: Throwable) : NetworkException(
    throwable,
    "We are facing a lot of request currently so our servers are busy kindly retry after some time"
)

class InternalApiError(throwable: Throwable) :
    NetworkException(throwable, "Sorry we encountered an error. We have logged it and will fix it soon")

class HttpCallFailureException(throwable: Throwable) :
    NetworkException(throwable, "Check your internet connection and retry")

class Unknown(throwable: Throwable) :
    NetworkException(throwable, "Unknown error. We have logged it and will fix it soon")

class AppExceptions {
    companion object {
        @JvmStatic
        fun parse(throwable: Throwable): AppException {
            return when (throwable) {
                is IOException -> HttpCallFailureException(throwable)
                is SocketTimeoutException -> NoNetworkException(throwable)
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