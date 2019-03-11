package com.mahesh.weather.utils

import org.mockito.Matchers.eq
import org.mockito.Mockito.`when`
import org.mockito.stubbing.OngoingStubbing

interface KotlinTestUtils {
    companion object {
        fun eqString(string: String): String {
            eq(string)
            return string
        }

        inline fun <reified T> whenever(methodCall: T): OngoingStubbing<T> {
            return if (T::class.java == Unit.javaClass) {
                `when`(methodCall).then { }
            } else {
                `when`(methodCall)
            }
        }
    }
}