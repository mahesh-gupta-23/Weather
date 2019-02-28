package com.mahesh.weather.service

import com.google.common.truth.Truth.assertThat
import com.mahesh.weather.service.models.Coord
import com.mahesh.weather.service.models.CurrentWeather
import com.mahesh.weather.service.repository.WeatherRepository
import com.mahesh.weather.testutils.BaseTest
import com.mahesh.weather.testutils.KotlinTestUtils.Companion.whenever
import com.mahesh.weather.testutils.Stubs
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.runners.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryTest : BaseTest() {
    @Mock
    private lateinit var mockWeatherRepository: WeatherRepository

    @Test
    fun getCurrentWeatherTest() {
        //Given
        val givenCoord = Coord(72.877655, 19.075983)

        whenever(
            mockWeatherRepository.getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!)
        ).thenReturn(Stubs.STUB_CURRENT_WEATHER)

        //When
        val givenResult: CurrentWeather? = mockWeatherRepository.getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!)

        //Then
        verify(mockWeatherRepository).getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!)
        assertThat(givenResult?.coord).isEqualTo(givenCoord)
        assertThat(givenResult?.weather?.get(0)).isEqualTo(Stubs.STUB_CURRENT_WEATHER.weather?.get(0))
        assertThat(givenResult?.base).isEqualTo(Stubs.STUB_CURRENT_WEATHER.base)
        assertThat(givenResult?.main).isEqualTo(Stubs.STUB_CURRENT_WEATHER.main)
        assertThat(givenResult?.wind).isEqualTo(Stubs.STUB_CURRENT_WEATHER.wind)
        assertThat(givenResult?.clouds).isEqualTo(Stubs.STUB_CURRENT_WEATHER.clouds)
        assertThat(givenResult?.rain).isEqualTo(Stubs.STUB_CURRENT_WEATHER.rain)
        assertThat(givenResult?.dt).isEqualTo(Stubs.STUB_CURRENT_WEATHER.dt)
        assertThat(givenResult?.sys).isEqualTo(Stubs.STUB_CURRENT_WEATHER.sys)
        assertThat(givenResult?.id).isEqualTo(Stubs.STUB_CURRENT_WEATHER.id)
        assertThat(givenResult?.name).isEqualTo(Stubs.STUB_CURRENT_WEATHER.name)
        assertThat(givenResult?.cod).isEqualTo(Stubs.STUB_CURRENT_WEATHER.cod)

    }
}