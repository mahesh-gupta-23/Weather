package com.mahesh.weather.app.extensions

import android.widget.ImageView
import com.mahesh.weather.R
import com.squareup.picasso.Picasso


fun Picasso.loadWeatherIn(iconName: String?, imageView: ImageView) {
    try {//Did to prevent crash when picasso is mocked then load function is not present
        load(imageView.context.getString(R.string.weather_image_url, iconName)).into(imageView)
    } catch (e: NullPointerException) {
        e.printStackTrace()
    }
}