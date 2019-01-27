package com.mahesh.weather.service.di

import com.mahesh.weather.service.Network
import org.koin.dsl.module.module

val NetworkModules = module(createOnStart = true) {
    single { Network() }
}