package com.mahesh.weather.forecast

import com.google.common.truth.Truth.assertThat
import com.mahesh.weather.app.coroutines.asynctaskmanager.AsyncTasksManager
import com.mahesh.weather.app.coroutines.asynctaskmanager.TestAsyncTasksManager
import com.mahesh.weather.service.repository.WeatherRepository
import com.mahesh.weather.utils.BaseTest
import com.mahesh.weather.utils.KotlinTestUtils.Companion.whenever
import com.mahesh.weather.utils.stubs.DataStubs
import com.mahesh.weather.utils.stubs.LocationStubs
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
    fun whenGivenTodayDate_itShouldBeFormattedInTheGivenFormat() {
        val mockDate: Date = mock()
        forecastModelInteractor.getTodayDateAndTimeFormatted(mockDate).run {
            assertThat(this).isEqualTo(dateFormatToDisplay.format(mockDate))
        }
    }

    @Test
    fun whenGetCurrentWeather_itShouldGetFromRepository() = runBlocking {
        whenever(
            mockWeatherRepository.getCurrentWeather(LocationStubs.LATITUDE, LocationStubs.LONGITUDE)
        ).thenReturn(DataStubs.STUB_CURRENT_WEATHER)

        forecastModelInteractor.getCurrentWeather(LocationStubs.LATITUDE, LocationStubs.LONGITUDE).run {
            verify(mockWeatherRepository).getCurrentWeather(LocationStubs.LATITUDE, LocationStubs.LONGITUDE)
            assertThat(this).isEqualTo(DataStubs.STUB_CURRENT_WEATHER)
        }
    }

    @Test
    fun whenGetForecast_itShouldGetFromRepositoryAndParse() = runBlocking {
        whenever(
            mockWeatherRepository.getWeatherForecast(LocationStubs.LATITUDE, LocationStubs.LONGITUDE)
        ).thenReturn(DataStubs.STUB_WEATHER_FORECAST)

        forecastModelInteractor.getDayForecast(LocationStubs.LATITUDE, LocationStubs.LONGITUDE).run {
            verify(mockWeatherRepository).getWeatherForecast(LocationStubs.LATITUDE, LocationStubs.LONGITUDE)
            assertThat(this!![0]).isEqualTo(DataStubs.STUB_DAY_FORECAST[0])
        }
    }
}