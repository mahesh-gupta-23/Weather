package com.mahesh.weather.service

import com.google.common.truth.Truth.assertThat
import com.mahesh.weather.service.repository.WeatherRepository
import com.mahesh.weather.testutils.BaseTest
import com.mahesh.weather.testutils.KotlinTestUtils.Companion.whenever
import com.mahesh.weather.testutils.Stubs
import com.mahesh.weather.testutils.Stubs.Companion.givenCoord
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryTest : BaseTest() {
    @Mock
    private lateinit var mockWeatherRepository: WeatherRepository

    @Before
    fun before() {
        whenever(
            mockWeatherRepository.getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!)
        ).thenReturn(Stubs.STUB_CURRENT_WEATHER)

        whenever(
            mockWeatherRepository.getWeatherForecast(givenCoord.lat!!, givenCoord.lon!!)
        ).thenReturn(Stubs.STUB_WEATHER_FORECAST)
    }

    @Test
    fun getCurrentWeatherTest() {
        mockWeatherRepository.getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!).run {
            assertThat(this).isEqualTo(Stubs.STUB_CURRENT_WEATHER)
        }
    }

    @Test
    fun getWeatherForecastTest() {
        mockWeatherRepository.getWeatherForecast(givenCoord.lat!!, givenCoord.lon!!).run {
            assertThat(this).isEqualTo(Stubs.STUB_WEATHER_FORECAST)
        }
    }
}