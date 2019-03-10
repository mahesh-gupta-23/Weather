package com.mahesh.weather.forecast

import com.google.common.truth.Truth.assertThat
import com.mahesh.weather.app.coroutines.asynctaskmanager.AsyncTasksManager
import com.mahesh.weather.app.coroutines.asynctaskmanager.TestAsyncTasksManager
import com.mahesh.weather.service.repository.WeatherRepository
import com.mahesh.weather.testutils.BaseTest
import com.mahesh.weather.testutils.KotlinTestUtils.Companion.whenever
import com.mahesh.weather.testutils.Stubs
import com.mahesh.weather.testutils.Stubs.Companion.givenCoord
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import java.text.SimpleDateFormat
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class ForecastModelInteractorTest : BaseTest() {

    @Spy
    private val asyncTasksManager: AsyncTasksManager = TestAsyncTasksManager()
    private val mockWeatherRepository: WeatherRepository = mock()

    private lateinit var forecastModelInteractor: ForecastModelInteractor

    private val dateFormatToDisplay: SimpleDateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())

    @Before
    fun before() {
        forecastModelInteractor = ForecastModelInteractor(asyncTasksManager, mockWeatherRepository)
    }

    @Test
    fun getTodayDateAndTimeTest() {
        forecastModelInteractor.getTodayDateAndTime().run {
            assertThat(this).isEqualTo(dateFormatToDisplay.format(Date()))
        }
    }

    @Test
    fun getCurrentWeatherTest() = runBlocking {
        whenever(
            mockWeatherRepository.getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!)
        ).thenReturn(Stubs.STUB_CURRENT_WEATHER)

        forecastModelInteractor.getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!).run {
            verify(mockWeatherRepository).getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!)
            assertThat(this).isEqualTo(Stubs.STUB_CURRENT_WEATHER)
        }
    }

    @Test
    fun getWeatherForecast() = runBlocking {
        whenever(
            mockWeatherRepository.getWeatherForecast(givenCoord.lat!!, givenCoord.lon!!)
        ).thenReturn(Stubs.STUB_WEATHER_FORECAST)

        forecastModelInteractor.getDayForecast(givenCoord.lat!!, givenCoord.lon!!).run {
            verify(mockWeatherRepository).getWeatherForecast(givenCoord.lat!!, givenCoord.lon!!)
            assertThat(this!![0]).isEqualTo(Stubs.STUB_DAY_FORECAST)
        }
    }
}