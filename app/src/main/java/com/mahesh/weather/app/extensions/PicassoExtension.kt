package com.mahesh.weather.app.extensions

import android.widget.ImageView
import com.mahesh.weather.R
import com.squareup.picasso.Picasso


fun Picasso.loadWeatherIn(iconName: String?, imageView: ImageView) {
    load(imageView.context.getString(R.string.weather_image_url, iconName)).into(imageView)
}