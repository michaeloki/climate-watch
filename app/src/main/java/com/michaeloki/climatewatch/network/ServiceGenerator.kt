package com.michaeloki.climatewatch.network

import com.michaeloki.climatewatch.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create())
    private val retrofit = retrofitBuilder.build()
    val weatherQueryApi: WeatherQueryApi = retrofit.create(WeatherQueryApi::class.java)
}