package com.michaeloki.climatewatch.utils

object Constants {
    val BASE_URL = "https://api.openweathermap.org/"
    val NETWORK_TIMEOUT = 3000
    val API_KEY =  "52f2ee9963e6396b5cac228a7a14a699" //"53f9d8e4213222cf517d86dc406d67fc"
    val units = "metric"
    const val SUCCESS_RESULT = 0

    const val FAILURE_RESULT = 1

    private const val PACKAGE_NAME = "com.michaeloki.climatewatch"

    const val RECEIVER = "$PACKAGE_NAME.RECEIVER"

    const val RESULT_DATA_KEY = "$PACKAGE_NAME.RESULT_DATA_KEY"

    const val LOCATION_DATA_EXTRA = "$PACKAGE_NAME.LOCATION_DATA_EXTRA"
}