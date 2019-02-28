package com.mahesh.weather.testutils

import com.mahesh.weather.service.models.*

interface Stubs {
    companion object {
        @JvmField
        val STUB_CURRENT_WEATHER = CurrentWeather(
            Coord(72.877655, 19.075983),
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
    }
}