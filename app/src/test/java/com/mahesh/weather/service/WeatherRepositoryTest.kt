package com.mahesh.weather.service

import com.google.common.truth.Truth.assertThat
import com.mahesh.weather.service.models.Coord
import com.mahesh.weather.service.models.CurrentWeather
import com.mahesh.weather.service.models.WeatherForecast
import com.mahesh.weather.service.repository.WeatherRepository
import com.mahesh.weather.testutils.BaseTest
import com.mahesh.weather.testutils.KotlinTestUtils.Companion.whenever
import com.mahesh.weather.testutils.Stubs
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.runners.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryTest : BaseTest() {
    @Mock
    private lateinit var mockWeatherRepository: WeatherRepository

    private lateinit var givenCoord: Coord

    @Before
    fun before() {
        //Given
        givenCoord = Coord(72.877655, 19.075983)

        whenever(
            mockWeatherRepository.getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!)
        ).thenReturn(Stubs.STUB_CURRENT_WEATHER)

        whenever(
            mockWeatherRepository.getWeatherForecast(givenCoord.lat!!, givenCoord.lon!!)
        ).thenReturn(Stubs.STUB_WEATHER_FORECAST)
    }

    @Test
    fun getCurrentWeatherTest() {
        //When
        val givenResult: CurrentWeather? = mockWeatherRepository.getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!)

        //Then
        verify(mockWeatherRepository).getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!)
        assertThat(givenResult).isEqualTo(Stubs.STUB_CURRENT_WEATHER)
    }

    @Test
    fun getWeatherForecastTest() {
        //When
        val givenResult: WeatherForecast? = mockWeatherRepository.getWeatherForecast(givenCoord.lat!!, givenCoord.lon!!)

        //Then
        verify(mockWeatherRepository).getWeatherForecast(givenCoord.lat!!, givenCoord.lon!!)
        assertThat(givenResult).isEqualTo(Stubs.STUB_WEATHER_FORECAST)
    }
}