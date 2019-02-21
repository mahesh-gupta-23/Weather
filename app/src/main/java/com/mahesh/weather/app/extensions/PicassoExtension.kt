package com.mahesh.weather.app.extensions

import android.widget.ImageView
import com.mahesh.weather.BuildConfig
import com.squareup.picasso.Picasso


fun Picasso.loadWeather(iconName: String?, imageView: ImageView) {
    this.load("${BuildConfig.WEATHER_ICON_BASE}$iconName.png").into(imageView)
}