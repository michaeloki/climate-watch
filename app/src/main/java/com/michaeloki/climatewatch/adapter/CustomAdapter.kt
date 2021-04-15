package com.michaeloki.climatewatch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import com.michaeloki.climatewatch.R
import com.michaeloki.climatewatch.model.Main
import com.michaeloki.climatewatch.model.Weather

class CustomAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mWeather: List<Weather>? = null
    private lateinit var mainWeather: Main

    @NonNull
    override fun onCreateViewHolder(
        @NonNull viewGroup: ViewGroup,
        i: Int
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.list_item_weather,
            viewGroup, false
        )
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull viewHolder: RecyclerView.ViewHolder, i: Int) {
        var weatherIconData = R.drawable.d01

        when (mWeather!![i].icon) {
            "01d" -> {
                weatherIconData = R.drawable.d01
            }
            "01n" -> {
                weatherIconData = R.drawable.n01
            }
            "02d" -> {
                weatherIconData = R.drawable.d02
            }
            "02n" -> {
                weatherIconData = R.drawable.n02
            }
            "03d" -> {
                weatherIconData = R.drawable.d03
            }
            "03n" -> {
                weatherIconData = R.drawable.n03
            }
            "04d" -> {
                weatherIconData = R.drawable.d04
            }
            "04n" -> {
                weatherIconData = R.drawable.n04
            }
            "09d" -> {
                weatherIconData = R.drawable.d09
            }
            "09n" -> {
                weatherIconData = R.drawable.n09
            }
            "10d" -> {
                weatherIconData = R.drawable.d10
            }
            "10n" -> {
                weatherIconData = R.drawable.n10
            }
            "11d" -> {
                weatherIconData = R.drawable.d11
            }
            "11n" -> {
                weatherIconData = R.drawable.n11
            }
            "13d" -> {
                weatherIconData = R.drawable.d13
            }
            "13n" -> {
                weatherIconData = R.drawable.n13
            }
            "50d" -> {
                weatherIconData = R.drawable.d50
            }
            "50n" -> {
                weatherIconData = R.drawable.n50
            }
        }

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
        Glide.with(viewHolder.itemView.context)
            .setDefaultRequestOptions(requestOptions)
            .load(weatherIconData)
            .into((viewHolder as CustomViewHolder).imageView)


        val title = viewHolder.title
        title.text = mWeather!![i].description.toString()


        val maxTemp = viewHolder.maxTemp
        maxTemp.text = "Max Temp " + mainWeather.tempMax.toString() + "˚C"


        val minTemp = viewHolder.minTemp
        minTemp.text = "Min Temp " + mainWeather.tempMin.toString() + "˚C"


        val pressure = viewHolder.pressure
        pressure.text = "Pressure " + mainWeather.pressure.toString()

        val humidity = viewHolder.humidity
        humidity.text = "Humidity " + mainWeather.humidity.toString()
    }

    fun setWeather(weather: List<Weather>) {
        mWeather = weather
    }

    fun setWeatherData(weatherData: Main) {
        mainWeather = weatherData
        notifyDataSetChanged()
    }


    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var title: TextView = itemView.findViewById(R.id.city_item)
        internal var maxTemp: TextView = itemView.findViewById(R.id.maxTemp)
        internal var minTemp: TextView = itemView.findViewById(R.id.minTemp)
        internal var pressure: TextView = itemView.findViewById(R.id.pressure)
        internal var humidity: TextView = itemView.findViewById(R.id.humidity)

        internal var imageView: ImageView = itemView.findViewById(R.id.weatherIcon)
    }

    override fun getItemCount(): Int {
        if (mWeather != null) {
            return mWeather!!.size
        }
        return 0
    }
}