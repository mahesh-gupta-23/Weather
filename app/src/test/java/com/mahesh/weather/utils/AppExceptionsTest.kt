package com.mahesh.weather.utils

import com.mahesh.weather.util.AppExceptions
import com.mahesh.weather.util.BaseTest
import com.mahesh.weather.util.NoNetworkException
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

@RunWith(JUnit4::class)
class AppExceptionsTest : BaseTest() {

    @Test
    fun whenIOException_returnNoInternetException() {
        val ioException = IOException()
        val parse = AppExceptions.parse(ioException)
        assert(parse is NoNetworkException)
    }
}