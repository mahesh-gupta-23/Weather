package com.mahesh.weather.service.di

import com.mahesh.weather.service.Network
import org.koin.dsl.module.module

val NetworkModule = module(createOnStart = true) {
    single { Network() }
}