package com.mahesh.weather.forecast

import android.util.Log
import com.mahesh.weather.app.TAG
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

    class GetForecastException constructor(val cityAndCountry: String) : RuntimeException()

    private val dayIdFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
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
            Log.d(TAG, "current")
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
                    var minTemperature = 0.0
                    var maxTemperature = 0.0
                    var isTemperatureFound = false

                    // Find the minimum and maximum temperatures for each day
                    dayForecasts.forEach {
                        it.main?.temp?.let { temp ->
                            if (!isTemperatureFound) {
                                isTemperatureFound = true
                                minTemperature = temp
                                maxTemperature = temp
                            } else {
                                minTemperature = min(minTemperature, temp)
                                maxTemperature = max(maxTemperature, temp)
                            }
                        }
                    }

                    val firstForecast: ThreeHoursWeatherForecast = dayForecasts[0]

                    // The day name can be extracted by any of the forecasts for the same day
                    val dayName =
                        if (firstForecast.dt != null) getDayName(firstForecast.dt * 1000) else throw GetForecastException(
                            "cityAndCountry"
                        )
                    val date = getDate(firstForecast.dt * 1000)

                    // Use the third weather condition as representative for the whole day
                    // and if not available, then use the first one.
                    val dayWeatherForecast: ThreeHoursWeatherForecast =
                        if (dayForecasts.size > 2) dayForecasts[2] else firstForecast
                    val description =
                        dayWeatherForecast.weather?.get(0)?.description ?: throw GetForecastException("cityAndCountry")
                    val icon = dayWeatherForecast.weather[0].icon ?: throw GetForecastException("cityAndCountry")

                    result.add(
                        DayForecast(
                            day = dayName,
                            date = date,
                            description = description,
                            icon = icon,
                            maxTemperature = if (isTemperatureFound) maxTemperature else null,
                            minTemperature = if (isTemperatureFound) minTemperature else null
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