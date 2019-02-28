package com.mahesh.weather.forecast

import com.google.common.truth.Truth.assertThat
import com.mahesh.weather.app.coroutines.asynctaskmanager.AsyncTasksManager
import com.mahesh.weather.app.coroutines.asynctaskmanager.TestAsyncTasksManager
import com.mahesh.weather.service.repository.WeatherRepository
import com.mahesh.weather.testutils.BaseTest
import com.mahesh.weather.testutils.KotlinTestUtils.Companion.whenever
import com.mahesh.weather.testutils.Stubs
import com.mahesh.weather.testutils.Stubs.Companion.givenCoord
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.runners.MockitoJUnitRunner
import java.text.SimpleDateFormat
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class ForecastModelInteractorTest : BaseTest() {

    @Spy
    private var asyncTasksManager: AsyncTasksManager = TestAsyncTasksManager()

    @Mock
    private lateinit var mockWeatherRepository: WeatherRepository

    private lateinit var subject: ForecastModelInteractor

    private val dateFormatToDisplay: SimpleDateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())

    @Before
    fun before() {
        subject = ForecastModelInteractor(asyncTasksManager, mockWeatherRepository)

        whenever(
            mockWeatherRepository.getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!)
        ).thenReturn(Stubs.STUB_CURRENT_WEATHER)

        whenever(
            mockWeatherRepository.getWeatherForecast(givenCoord.lat!!, givenCoord.lon!!)
        ).thenReturn(Stubs.STUB_WEATHER_FORECAST)

    }

    @Test
    fun getCurrentWeatherTest() = runBlocking {
        subject.getCurrentWeather(givenCoord.lat!!, givenCoord.lon!!).run {
            assertThat(this).isEqualTo(Stubs.STUB_CURRENT_WEATHER)
        }
    }

    @Test
    fun getWeatherForecast() = runBlocking {
        subject.getForecast(givenCoord.lat!!, givenCoord.lon!!).run {
            assertThat(this!![0].date).isEqualTo("27/02")
            assertThat(this[0].day).isEqualTo("Wed")
            assertThat(this[0].icon).isEqualTo("01d")
            assertThat(this[0].maxTemperature).isEqualTo(27.1)
            assertThat(this[0].minTemperature).isEqualTo(25.14)
        }
    }

    @Test
    fun createForecastAdapterEntity() = runBlocking {
        subject.createForecastAdapterEntity(subject.getForecast(givenCoord.lat!!, givenCoord.lon!!)).run {
            val adapterEntity = subject.adapterEntityList[0]
            assertThat(subject.adapterEntityList.size).isEqualTo(1)
            assertThat(adapterEntity.date).isEqualTo("27/02")
            assertThat(adapterEntity.day).isEqualTo("Wed")
            assertThat(adapterEntity.iconName).isEqualTo("01d")
            assertThat(adapterEntity.maxTemperature).isEqualTo(27.1)
            assertThat(adapterEntity.minTemperature).isEqualTo(25.14)
        }
    }

    @Test
    fun getTodayDateAndTimeTest() {
        subject.getTodayDateAndTime().run {
            assertThat(this).isEqualTo(dateFormatToDisplay.format(Date()))
        }
    }
}