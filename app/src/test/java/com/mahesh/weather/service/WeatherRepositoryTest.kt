package com.mahesh.weather.service

import com.google.common.truth.Truth.assertThat
import com.mahesh.weather.service.repository.WeatherRepository
import com.mahesh.weather.utils.BaseTest
import com.mahesh.weather.utils.KotlinTestUtils.Companion.whenever
import com.mahesh.weather.utils.stubs.Stubs
import com.mahesh.weather.utils.stubs.Stubs.Companion.givenCoord
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryTest : BaseTest() {
    private val mockWeatherRepository: WeatherRepository = mock()

    @Test
    fun whenGetCurrentWeather_itShouldInvokeApi() = runBlocking {
        whenever(
            mockWeatherRepository.getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!)
        ).thenReturn(Stubs.STUB_CURRENT_WEATHER)

        mockWeatherRepository.getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!).run {
            verify(mockWeatherRepository).getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!)
            assertThat(this).isEqualTo(Stubs.STUB_CURRENT_WEATHER)
        }
    }

    @Test
    fun whenGetForecast_itShouldInvokeApi() = runBlocking {
        whenever(
            mockWeatherRepository.getWeatherForecast(givenCoord.lat!!, givenCoord.lon!!)
        ).thenReturn(Stubs.STUB_WEATHER_FORECAST)

        mockWeatherRepository.getWeatherForecast(givenCoord.lat!!, givenCoord.lon!!).run {
            verify(mockWeatherRepository).getWeatherForecast(givenCoord.lat!!, givenCoord.lon!!)
            assertThat(this).isEqualTo(Stubs.STUB_WEATHER_FORECAST)
        }
    }
}