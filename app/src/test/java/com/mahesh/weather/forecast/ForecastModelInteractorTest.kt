package com.mahesh.weather.forecast

import com.google.common.truth.Truth.assertThat
import com.mahesh.weather.app.coroutines.asynctaskmanager.AsyncTasksManager
import com.mahesh.weather.app.coroutines.asynctaskmanager.TestAsyncTasksManager
import com.mahesh.weather.service.models.Coord
import com.mahesh.weather.service.models.CurrentWeather
import com.mahesh.weather.service.repository.WeatherRepository
import com.mahesh.weather.testutils.BaseTest
import com.mahesh.weather.testutils.KotlinTestUtils.Companion.whenever
import com.mahesh.weather.testutils.Stubs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Spy
import org.mockito.runners.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class ForecastModelInteractorTest : BaseTest() {

    @Spy
    private var asyncTasksManager: AsyncTasksManager = TestAsyncTasksManager()
    @Mock
    private lateinit var mockWeatherRepository: WeatherRepository

    private lateinit var subject: ForecastModelInteractor

    private lateinit var givenCoord: Coord

    @Before
    fun before() {
        subject = ForecastModelInteractor(asyncTasksManager, mockWeatherRepository)

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
    fun getCurrentWeatherTest() = runBlocking {
        //When
        val givenResult: CurrentWeather? = subject.getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!)

        //Then
        verify(mockWeatherRepository).getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!)
        assertThat(givenResult).isEqualTo(Stubs.STUB_CURRENT_WEATHER)
    }

}