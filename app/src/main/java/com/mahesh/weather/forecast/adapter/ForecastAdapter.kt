package com.mahesh.weather.forecast.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mahesh.weather.R
import com.mahesh.weather.databinding.ForecastRowBinding
import com.mahesh.weather.forecast.ForecastContract

class ForecastAdapter(private val context: Context?, private val presenter: ForecastContract.AdapterPresenter) :
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

    class WeatherForecastViewHolder(binding: ForecastRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(current: ForecastAdapterModel) {

        }

    }
}