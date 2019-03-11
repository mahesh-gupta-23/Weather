package com.mahesh.weather.forecast

import com.mahesh.weather.app.coroutines.asynctaskmanager.AsyncTasksManager
import com.mahesh.weather.service.models.CurrentWeather
import com.mahesh.weather.service.models.DayForecast
import com.mahesh.weather.service.models.ThreeHoursWeatherForecast
import com.mahesh.weather.service.models.WeatherForecast
import com.mahesh.weather.service.repository.WeatherRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class ForecastModelInteractor @Inject constructor(
    private val asyncTasksManager: AsyncTasksManager, private val weatherRepository: WeatherRepository
) : ForecastContract.ModelInteractor {

    private val dayIdFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    private var dayNameFormat: SimpleDateFormat = SimpleDateFormat("EE", Locale.getDefault())
    private var dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
    private var mainDateFormat: SimpleDateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())

    init {
        dayIdFormat.timeZone = TimeZone.getTimeZone("UTC")
        dayNameFormat.timeZone = TimeZone.getDefault()
        dateFormat.timeZone = TimeZone.getDefault()
        mainDateFormat.timeZone = TimeZone.getDefault()
    }

    override suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeather? {
        return weatherRepository.getCurrentWeather(lat, lon)
    }

    override suspend fun getDayForecast(lat: Double, lon: Double): List<DayForecast>? {
        val forecast = weatherRepository.getWeatherForecast(lat, lon)
        return mapWeatherForecastToDayForecastList(forecast)
    }

    override fun getTodayDateAndTimeFormatted(date: Date): String {
        return mainDateFormat.format(date)
    }

    private suspend fun mapWeatherForecastToDayForecastList(forecast: WeatherForecast?): List<DayForecast> =
        asyncTasksManager.asyncAwait {
            val result: MutableList<DayForecast> = mutableListOf()
            if (forecast?.list != null && forecast.list.isNotEmpty()) {
                // Group the forecasts by day
                getForecastsGroupedByDay(forecast.list).forEach { _, dayForecasts ->
                    if (dayForecasts.isNotEmpty()) {
                        // Use the third weather condition as representative for the whole day
                        // and if not available, then use the first one.
                        val dayWeatherForecast = if (dayForecasts.size > 2) dayForecasts[2] else dayForecasts[0]
                        result.add(
                            DayForecast(
                                day = getDayName(dayForecasts[0].dt!! * 1000),
                                date = getDate(dayForecasts[0].dt!! * 1000),
                                icon = dayWeatherForecast.weather?.get(0)?.icon,
                                maxTemperature = getMaxTempFor(dayForecasts),
                                minTemperature = getMinTempFor(dayForecasts)
                            )
                        )
                    }
                }
            }
            return@asyncAwait result
        }

    private fun getMaxTempFor(dayForecasts: List<ThreeHoursWeatherForecast>): Double? {
        var maxTemp: Double? = null
        dayForecasts.forEach {
            it.main?.tempMax?.let { currentMax ->
                maxTemp = max(maxTemp ?: 0.0, currentMax)
            }
        }
        return maxTemp
    }

    private fun getMinTempFor(dayForecasts: List<ThreeHoursWeatherForecast>): Double? {
        var minTemp: Double? = null
        dayForecasts.forEach {
            it.main?.tempMin?.let { currentMin ->
                minTemp = if (minTemp == null) currentMin else min(minTemp ?: 0.0, currentMin)
            }
        }
        return minTemp
    }

    private fun getForecastsGroupedByDay(forecasts: List<ThreeHoursWeatherForecast>): Map<String, List<ThreeHoursWeatherForecast>> {
        return forecasts
            .filter { it.dt != null }
            .groupBy { getDayId(it.dt!! * 1000) }
    }

    private fun getDayId(utcTimeMillis: Long): String {
        return dayIdFormat.format(Date(utcTimeMillis))
    }

    private fun getDayName(utcTimeMillis: Long): String {
        return dayNameFormat.format(Date(utcTimeMillis))
    }

    private fun getDate(utcTimeMillis: Long): String {
        return dateFormat.format(Date(utcTimeMillis))
    }
}