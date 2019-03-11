package com.mahesh.weather.utils

import org.junit.Rule
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

open class BaseTest {

    @Rule
    @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()
}