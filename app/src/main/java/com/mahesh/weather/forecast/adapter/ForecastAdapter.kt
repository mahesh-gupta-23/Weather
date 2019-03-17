package com.mahesh.weather.forecast.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mahesh.weather.R
import com.mahesh.weather.app.TAG
import com.mahesh.weather.app.extensions.loadWeatherIn
import com.mahesh.weather.databinding.ForecastRowBinding
import com.mahesh.weather.forecast.ForecastContract
import com.squareup.picasso.Picasso

open class ForecastAdapter(
    private val context: Context?,
    private val presenter: ForecastContract.AdapterPresenter,
    private val picasso: Picasso
) :
    RecyclerView.Adapter<ForecastAdapter.WeatherForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherForecastViewHolder {
        return WeatherForecastViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.forecast_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return presenter.getAdapterEntityCount()
    }

    override fun onBindViewHolder(holder: WeatherForecastViewHolder, position: Int) {
        Log.d(TAG, "position $position")
        holder.bind(presenter.getAdapterEntity(position))
    }

    inner class WeatherForecastViewHolder(private val binding: ForecastRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(current: ForecastAdapterModel) = with(current) {
            binding.tvDay.text = day
            binding.tvDate.text = date
            picasso.loadWeatherIn(iconName, binding.ivWeather)
            binding.tvMaxTemp.text = context?.getString(R.string.temp, maxTemperature.toString())
            binding.tvMinTemp.text = context?.getString(R.string.temp, minTemperature.toString())
        }
    }
}