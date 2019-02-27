package com.mahesh.weather.forecast

import com.mahesh.weather.app.coroutines.asynctaskmanager.AsyncTasksManager
import com.mahesh.weather.forecast.adapter.ForecastAdapterModel
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
    private val asyncTasksManager: AsyncTasksManager,
    private val weatherRepository: WeatherRepository
) :
    ForecastContract.ModelInteractor {

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

    override val adapterEntityList: MutableList<ForecastAdapterModel> = mutableListOf()

    override suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeather? {
        return asyncTasksManager.asyncAwait {
            weatherRepository.getCurrentWeather(lat, lon)
        }
    }

    override suspend fun getForecast(lat: Double, lon: Double): List<DayForecast>? {
        val forecast = asyncTasksManager.asyncAwait {
            weatherRepository.getWeatherForecast(lat, lon)
        }
        return mapWeatherForecastToDayForecastList(forecast)
    }

    override fun getTodayDateAndTime(): String {
        return mainDateFormat.format(Date())
    }

    private suspend fun mapWeatherForecastToDayForecastList(
        forecast: WeatherForecast?
    ): List<DayForecast> = asyncTasksManager.asyncAwait {
        val result: MutableList<DayForecast> = mutableListOf()
        if (forecast?.list != null && forecast.list.isNotEmpty()) {
            // Group the forecasts by day
            val forecastsGroupedByDay = getForecastsGroupedByDay(forecast.list)
            forecastsGroupedByDay.forEach { _, dayForecasts ->
                if (dayForecasts.isNotEmpty()) {
                    var minTemperature: Double? = null
                    var maxTemperature: Double? = null

                    // Find the minimum and maximum temperatures for each day
                    dayForecasts.forEach {
                        it.main?.tempMin?.let { minTemp ->
                            minTemperature =
                                if (minTemperature == null) minTemp else min(minTemperature ?: 0.0, minTemp)
                        }
                    }
                    dayForecasts.forEach {
                        it.main?.tempMax?.let { maxTemp ->
                            maxTemperature = max(maxTemperature ?: 0.0, maxTemp)
                        }
                    }

                    val firstForecast: ThreeHoursWeatherForecast = dayForecasts[0]

                    // The day name can be extracted by any of the forecasts for the same day
                    val dayName = getDayName(firstForecast.dt!! * 1000)
                    val date = getDate(firstForecast.dt * 1000)

                    // Use the third weather condition as representative for the whole day
                    // and if not available, then use the first one.
                    val dayWeatherForecast: ThreeHoursWeatherForecast =
                        if (dayForecasts.size > 2) dayForecasts[2] else firstForecast
                    val icon = dayWeatherForecast.weather?.get(0)?.icon

                    result.add(
                        DayForecast(
                            day = dayName,
                            date = date,
                            icon = icon,
                            maxTemperature = maxTemperature,
                            minTemperature = minTemperature
                        )
                    )
                }
            }
        }

        return@asyncAwait result
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

    override fun createForecastAdapterEntity(forecast: List<DayForecast>?) {
        forecast?.forEach {
            if (adapterEntityList.size < 5) {
                adapterEntityList.add(
                    ForecastAdapterModel(
                        day = it.day,
                        date = it.date,
                        iconName = it.icon,
                        maxTemperature = it.maxTemperature,
                        minTemperature = it.minTemperature
                    )
                )
            }
        }
    }

}