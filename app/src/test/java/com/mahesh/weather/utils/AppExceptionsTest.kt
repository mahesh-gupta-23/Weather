package com.mahesh.weather.utils

import com.mahesh.weather.helper.*
import com.mahesh.weather.util.BaseTest
import com.mahesh.weather.util.stubs.DataStubs
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@RunWith(JUnit4::class)
class AppExceptionsTest : BaseTest() {

    @Test
    fun whenIOException_shouldReturnNoInternetException() {
        with(AppExceptions.parse(IOException())) {
            assert(this is NoNetworkException)
            assert(this is NonFatalException)
            assert(this !is FatalException)
        }
    }

    @Test
    fun whenSocketTimeoutException_shouldReturnNoInternetException() {
        with(AppExceptions.parse(SocketTimeoutException())) {
            assert(this is NoNetworkException)
            assert(this is NonFatalException)
            assert(this !is FatalException)
        }
    }

    @Test
    fun whenUnknownHostException_shouldReturnServerException() {
        with(AppExceptions.parse(UnknownHostException())) {
            assert(this is ServerException)
            assert(this !is NonFatalException)
            assert(this is FatalException)
        }
    }

    @Test
    fun `when HttpException 400 or 404 to 428 should return InternalApiError`() {
        with(AppExceptions.parse(DataStubs.getHttpException(400))) {
            assert(this is InternalApiError)
            assert(this !is NonFatalException)
            assert(this is FatalException)
        }
        with(AppExceptions.parse(DataStubs.getHttpException(404))) {
            assert(this is InternalApiError)
            assert(this !is NonFatalException)
            assert(this is FatalException)
        }
        with(AppExceptions.parse(DataStubs.getHttpException(428))) {
            assert(this is InternalApiError)
            assert(this !is NonFatalException)
            assert(this is FatalException)
        }
        with(AppExceptions.parse(DataStubs.getHttpException(420))) {
            assert(this is InternalApiError)
            assert(this !is NonFatalException)
            assert(this is FatalException)
        }
    }

    @Test
    fun `when HttpException 429 to 599 should return ServerException`() {
        with(AppExceptions.parse(DataStubs.getHttpException(429))) {
            assert(this is ServerException)
            assert(this !is NonFatalException)
            assert(this is FatalException)
        }
        with(AppExceptions.parse(DataStubs.getHttpException(500))) {
            assert(this is ServerException)
            assert(this !is NonFatalException)
            assert(this is FatalException)
        }
        with(AppExceptions.parse(DataStubs.getHttpException(599))) {
            assert(this is ServerException)
            assert(this !is NonFatalException)
            assert(this is FatalException)
        }
    }

    @Test
    fun `when HttpException out of scope should return Unknown`() {
        with(AppExceptions.parse(DataStubs.getHttpException(600))) {
            assert(this is Unknown)
            assert(this !is NonFatalException)
            assert(this is FatalException)
        }
    }

    @Test
    fun `when out of scope should return Unknown`() {
        with(AppExceptions.parse(NullPointerException())) {
            assert(this is Unknown)
            assert(this !is NonFatalException)
            assert(this is FatalException)
        }
    }
}