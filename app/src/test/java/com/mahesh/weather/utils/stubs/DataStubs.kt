package com.mahesh.weather.utils.stubs

import com.mahesh.weather.forecast.adapter.ForecastAdapterModel
import com.mahesh.weather.service.models.*

class DataStubs {
    companion object {

        @JvmField
        val STUB_CURRENT_WEATHER = CurrentWeather(
            Coord(LocationStubs.LONGITUDE, LocationStubs.LATITUDE),
            listOf(Weather(711, "Smoke", "smoke", "50d")),
            "stations",
            Main(29.0, 29.0, 29.0, 1009.0, 0.0, 0.0, 0, 0.0),
            Wind(6.7, 300.0),
            Clouds(0),
            Rain(0.0),
            1510521300,
            Sys(1, 9052, 0.0048, "IN", 1551317314, 1551359621, ""),
            8131499,
            "Konkan Division",
            200
        )

        @JvmField
        val STUB_WEATHER_FORECAST = WeatherForecast(
            cod = "200",
            message = 0.0045,
            cnt = 40,
            list = listOf(
                ThreeHoursWeatherForecast(
                    dt = 1551268800,
                    main = Main(
                        temp = 27.1,
                        tempMin = 25.14,
                        tempMax = 27.1,
                        pressure = 1010.09,
                        seaLevel = 1010.09,
                        grndLevel = 1009.51,
                        humidity = 82,
                        tempKf = 1.96
                    ),
                    weather = listOf(
                        Weather(id = 800, main = "Clear", description = "clear sky", icon = "01d")
                    ),
                    clouds = Clouds(0),
                    wind = Wind(5.19, 324.003),
                    sys = Sys(null, null, null, null, null, null, "d"),
                    dtTxt = "2019-02-27 12:00:00"
                )
            ),
            city = City(id = 8131499, name = "Konkan Division", coord = Coord(72.8691, 19.0756), country = "IN")
        )

        @JvmField
        val STUB_DAY_FORECAST: List<DayForecast> = listOf(
            DayForecast(day = "Wed", date = "27/02", icon = "01d", maxTemperature = 27.1, minTemperature = 25.14)
        )

        @JvmField
        val ADAPTER_ENTITY: ForecastAdapterModel = with(STUB_DAY_FORECAST[0]) {
            ForecastAdapterModel(day, date, icon, maxTemperature, minTemperature)
        }
    }
}