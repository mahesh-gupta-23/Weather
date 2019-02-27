package com.mahesh.weather.forecast.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mahesh.weather.R
import com.mahesh.weather.app.extensions.loadWeather
import com.mahesh.weather.databinding.ForecastRowBinding
import com.mahesh.weather.forecast.ForecastContract
import com.squareup.picasso.Picasso

class ForecastAdapter(
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
        holder.bind(presenter.getAdapterEntity(position))
    }

    inner class WeatherForecastViewHolder(private val binding: ForecastRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(current: ForecastAdapterModel) {
            binding.tvDay.text = current.day
            binding.tvDate.text = current.date
            picasso.loadWeather(current.iconName, binding.ivWeather)
            binding.tvMaxTemp.text = context?.getString(R.string.temp, current.maxTemperature.toString())
            binding.tvMinTemp.text = context?.getString(R.string.temp, current.minTemperature.toString())
        }
    }
}