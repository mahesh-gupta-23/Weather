package com.mahesh.weather.app.extensions

import android.view.View

fun View.toggleVisibility(show: Boolean) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}