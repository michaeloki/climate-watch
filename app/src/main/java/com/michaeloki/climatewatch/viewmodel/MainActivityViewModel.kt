package com.michaeloki.climatewatch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.michaeloki.climatewatch.model.Main
import com.michaeloki.climatewatch.model.Weather
import com.michaeloki.climatewatch.repositories.WeatherRepository


class MainActivityViewModel : ViewModel() {
    private val mWeatherRepository: WeatherRepository = WeatherRepository.getInstance()
    private var mIsQueryingWeather: Boolean
    private var mIsViewingWeather = false
    val weather: LiveData<List<Weather>>
        get() = mWeatherRepository.weather

    val weatherData: LiveData<Main>
        get() = mWeatherRepository.weatherModel


    fun isViewingWeather(): Boolean {
        return mIsViewingWeather
    }

    fun setIsQueryingWeather(mIsQueryingWeather: Boolean) {
        this.mIsQueryingWeather = mIsQueryingWeather
    }

    fun searchWeatherApi(lat: Double, lon: Double, APPID: String, units: String) {
        mIsViewingWeather = true
        mWeatherRepository.searchWeatherApi(lat, lon, APPID, units)
        mIsQueryingWeather = true
    }


    init {
        mIsQueryingWeather = false
    }
}