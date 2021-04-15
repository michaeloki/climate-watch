package com.michaeloki.climatewatch.repositories

import androidx.lifecycle.LiveData
import com.michaeloki.climatewatch.model.Main
import com.michaeloki.climatewatch.model.Weather
import com.michaeloki.climatewatch.model.WeatherModel
import com.michaeloki.climatewatch.network.ApiClient


class WeatherRepository private constructor() {
    private var mApiClient: ApiClient = ApiClient.getInstance()
    private  var mLat: Double = 0.0
    private  var mLon: Double = 0.0
    private lateinit var mAPPID: String
    private lateinit var mUnits: String

    val weather: LiveData<List<Weather>>
        get() {
            return mApiClient.weather
        }

    val weatherModel: LiveData<Main>
        get() {
            return mApiClient.weatherCondition
        }


    fun searchWeatherApi(lat: Double,lon: Double,APPID: String, units: String) {
        mLat = lat
        mLon = lon
        mAPPID = APPID
        mUnits = units
        mApiClient.searchWeatherApi(lat,lon,APPID, units)
    }


    companion object {
        private lateinit var instance: WeatherRepository
        fun getInstance(): WeatherRepository {

            instance = WeatherRepository()

            return instance
        }
    }
}