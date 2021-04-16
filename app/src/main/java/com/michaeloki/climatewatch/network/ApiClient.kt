package com.michaeloki.climatewatch.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.michaeloki.climatewatch.AppExecutors
import com.michaeloki.climatewatch.model.Main
import com.michaeloki.climatewatch.model.WeatherModel
import com.michaeloki.climatewatch.model.Weather
import com.michaeloki.climatewatch.utils.Constants.NETWORK_TIMEOUT
import retrofit2.Call
import java.util.concurrent.TimeUnit

class ApiClient private constructor() {
    private var mWeather: MutableLiveData<List<Weather>> = MutableLiveData()
    private var mWeatherConditions: MutableLiveData<Main> = MutableLiveData()

    private lateinit var mRetrieveWeatherRunnable: RetrieveWeatherRunnable

    private var weatherList = ArrayList<Weather>()

    val weather: LiveData<List<Weather>>
        get() {
            return mWeather
        }

    val weatherCondition: LiveData<Main>
        get() {
            return mWeatherConditions
        }


    fun searchWeatherApi(lat: Double, lon: Double, APPID: String, units: String) {
        mRetrieveWeatherRunnable = RetrieveWeatherRunnable(lat, lon, APPID, units)
        val handler = AppExecutors.get().networkIO().submit(mRetrieveWeatherRunnable)

        AppExecutors.get().networkIO()
                .schedule({ handler.cancel(true) }, NETWORK_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
    }


    private inner class RetrieveWeatherRunnable(private val lat: Double,
                                                private val lon: Double,
                                                private val APPID: String,
                                                private var units: String
    ) : Runnable {
        private var cancelRequest: Boolean = false

        init {
            cancelRequest = false
        }

        override fun run() {
            try {
                val response = getWeather(lat, lon, APPID, units)!!.execute()
                if (cancelRequest) {
                    return
                }
                if (response.code() == 200) {
                    weatherList = ArrayList((response.body() as WeatherModel).weather)
                    try {
                        mWeather.postValue(weatherList)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        if (mWeather.value == null) {
                            mWeather.postValue(weatherList)
                        }
                        mWeatherConditions.postValue((response.body() as WeatherModel).main)
                    }
                } else {
                    mWeather.postValue(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                mWeather.postValue(null)
            }
        }

        private fun getWeather(lat: Double, lon: Double, APPID: String, units: String): Call<WeatherModel>? {
            return ServiceGenerator.weatherQueryApi.getWeatherInfo(
                    lat,
                    lon,
                    APPID,
                    units
            )
        }
    }

    companion object {
        private lateinit var instance: ApiClient
        fun getInstance(): ApiClient {
            instance = ApiClient()
            return instance
        }
    }
}