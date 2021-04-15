package com.michaeloki.climatewatch.network

import android.app.IntentService
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import com.michaeloki.climatewatch.R
import com.michaeloki.climatewatch.utils.Constants



class GetAddressIntentService : IntentService("FetchAddress") {

    private val TAG = "GetAddressService"

    private var receiver: ResultReceiver? = null

    override fun onHandleIntent(intent: Intent?) {
        var errorMessage = ""

        receiver = intent?.getParcelableExtra(Constants.RECEIVER)

        if (intent == null || receiver == null) {
            Log.i(TAG, "No receiver received. There is no place to send the output.")
            return
        }

        val location = intent.getParcelableExtra<Location>(Constants.LOCATION_DATA_EXTRA)

        if (location == null) {
            errorMessage = getString(R.string.no_location_data_provided)
            Log.i(TAG, errorMessage)
            sendResultToReceiver(Constants.FAILURE_RESULT, 0.0, 0.0)
            return
        }

            sendResultToReceiver(Constants.SUCCESS_RESULT,location.latitude,location.longitude
                )
    }

    private fun sendResultToReceiver(resultCode: Int, lat: Double, lon: Double) {
        val bundle = Bundle().apply { putString(Constants.RESULT_DATA_KEY, "$lat $lon") }
        receiver?.send(resultCode, bundle)
    }
}