package com.michaeloki.climatewatch

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class AppExecutors {
    private val mNetworkIO = Executors.newScheduledThreadPool(3)
    fun networkIO(): ScheduledExecutorService {
        return mNetworkIO
    }

    companion object {
        private lateinit var instance: AppExecutors
        fun get(): AppExecutors {
            instance = AppExecutors()
            return instance
        }
    }
}