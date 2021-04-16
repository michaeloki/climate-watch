package com.michaeloki.climatewatch.network

import com.michaeloki.climatewatch.model.*
import retrofit2.Call
import retrofit2.http.*


interface WeatherQueryApi {
    @GET("data/2.5/weather/")
    fun getWeatherInfo(
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            @Query("APPID") APPID: String?,
            @Query("units") units: String?,
    ): Call<WeatherModel>?
}