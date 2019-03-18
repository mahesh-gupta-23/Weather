package com.mahesh.weather.service

import com.google.common.truth.Truth.assertThat
import com.mahesh.weather.service.repository.WeatherRepository
import com.mahesh.weather.util.stubs.DataStubs
import com.mahesh.weather.util.stubs.LocationStubs
import com.mahesh.weather.util.BaseTest
import com.mahesh.weather.util.KotlinTestUtils.Companion.whenever
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
            mockWeatherRepository.getCurrentWeather(LocationStubs.LATITUDE, LocationStubs.LONGITUDE)
        ).thenReturn(DataStubs.STUB_CURRENT_WEATHER)

        with(mockWeatherRepository.getCurrentWeather(LocationStubs.LATITUDE, LocationStubs.LONGITUDE)) {
            verify(mockWeatherRepository).getCurrentWeather(LocationStubs.LATITUDE, LocationStubs.LONGITUDE)
            assertThat(this).isEqualTo(DataStubs.STUB_CURRENT_WEATHER)
        }
    }

    @Test
    fun whenGetForecast_itShouldInvokeApi() = runBlocking {
        whenever(
            mockWeatherRepository.getWeatherForecast(LocationStubs.LATITUDE, LocationStubs.LONGITUDE)
        ).thenReturn(DataStubs.STUB_WEATHER_FORECAST)

        with(mockWeatherRepository.getWeatherForecast(LocationStubs.LATITUDE, LocationStubs.LONGITUDE)) {
            verify(mockWeatherRepository).getWeatherForecast(LocationStubs.LATITUDE, LocationStubs.LONGITUDE)
            assertThat(this).isEqualTo(DataStubs.STUB_WEATHER_FORECAST)
        }
    }
}