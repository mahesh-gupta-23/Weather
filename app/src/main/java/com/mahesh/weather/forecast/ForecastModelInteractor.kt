package com.mahesh.weather.forecast

import com.mahesh.weather.app.coroutines.asynctaskmanager.AsyncTasksManager
import com.mahesh.weather.service.models.CurrentWeather
import com.mahesh.weather.service.repository.WeatherRepository
import javax.inject.Inject

class ForecastModelInteractor @Inject constructor(
    private val asyncTasksManager: AsyncTasksManager,
    private val weatherRepository: WeatherRepository
) :
    ForecastContract.ModelInteractor {

    override suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeather? {
        return asyncTasksManager.asyncAwait {
            weatherRepository.getCurrentWeather(lat, lon)
        }
    }

}