package com.michaeloki.climatewatch

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.michaeloki.climatewatch.adapter.CustomAdapter
import com.michaeloki.climatewatch.network.GetAddressIntentService
import com.michaeloki.climatewatch.network.NetworkStatus
import com.michaeloki.climatewatch.utils.Constants
import com.michaeloki.climatewatch.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item_weather.*
import kotlinx.android.synthetic.main.list_item_weather.view.*
import java.util.*


open class MainActivity : AppCompatActivity() {
    private lateinit var mMainActivityViewModel: MainActivityViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: CustomAdapter
    private val TAG = MainActivity::class.java.simpleName

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 17
    private val ADDRESS_REQUESTED_KEY = "address-request-pending"
    private val LOCATION_ADDRESS_KEY = "location-address"
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null

    private var addressRequested = false
    private var addressOutput = ""
    private var cityAddress = ""

    private lateinit var resultReceiver: AddressResultReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerView = findViewById(R.id.list_locations)

        mMainActivityViewModel =
                ViewModelProvider(this@MainActivity).get(MainActivityViewModel::class.java)

        addressOutput = ""
        updateValuesFromBundle(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        initRecyclerView()
        subscribeObservers()
        refreshButton.setOnClickListener {
            getAddress()
        }
    }

    public override fun onStart() {
        super.onStart()
        val mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000)
        checkPermissions()
    }


    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        savedInstanceState ?: return

        ADDRESS_REQUESTED_KEY.let {
            if (savedInstanceState.keySet().contains(it)) {
                addressRequested = savedInstanceState.getBoolean(it)
            }
        }

        LOCATION_ADDRESS_KEY.let {
            if (savedInstanceState.keySet().contains(it)) {
                addressOutput = savedInstanceState.getString(it).toString()
                Log.i(TAG, "=====>>>>>:::: $addressOutput")
            }
        }
    }


    private fun subscribeObservers() {
        mMainActivityViewModel.weather.observe(this,
                { weather ->
                    if (weather != null) {
                        if (mMainActivityViewModel.isViewingWeather()) {
                            mAdapter.setWeather(weather)
                            loadingIndicator.visibility = View.INVISIBLE
                            mMainActivityViewModel.setIsQueryingWeather(false)
                        }
                        weatherCard.visibility = View.VISIBLE
                        networkError.visibility = View.GONE
                    } else {
                        if (!NetworkStatus().internetConnectionAvailable(3000L)) {
                            noSearchOutput.visibility = View.GONE
                            networkError.visibility = View.VISIBLE
                            return@observe Snackbar.make(
                                    findViewById(android.R.id.content),
                                    getString(R.string.internetProblem),
                                    Snackbar.LENGTH_LONG
                            ).show()
                        } else {
                            noSearchOutput.visibility = View.VISIBLE
                            networkError.visibility = View.GONE
                            return@observe Snackbar.make(
                                    findViewById(android.R.id.content),
                                    getString(R.string.noSearchResults),
                                    Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                })


        mMainActivityViewModel.weatherData.observe(this,
                { weatherData ->
                    if (weatherData != null) {
                        if (mMainActivityViewModel.isViewingWeather()) {
                            mAdapter.setWeatherData(weatherData)
                            mMainActivityViewModel.setIsQueryingWeather(false)
                            try {
                                currentCity.text = cityAddress
                            } catch (e: Exception) {
                                println(":::::YES show me here $cityAddress !!")
                            }

                        }
                    }
                })
    }


    private fun initRecyclerView() {
        mAdapter = CustomAdapter()
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState
        with(savedInstanceState) {
            putBoolean(ADDRESS_REQUESTED_KEY, addressRequested)
            putString(LOCATION_ADDRESS_KEY, addressOutput)
        }

        super.onSaveInstanceState(savedInstanceState)
    }

    private fun updateUIWidgets() {
        if (addressRequested) {
            loadingIndicator.visibility = View.VISIBLE
        } else {
            loadingIndicator.visibility = View.GONE
        }
    }

    private inner class AddressResultReceiver(
            handler: Handler
    ) : ResultReceiver(handler) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {

            addressOutput = resultData.getString(Constants.RESULT_DATA_KEY).toString()
            getAddress()

            if (resultCode == Constants.SUCCESS_RESULT) {
                Toast.makeText(this@MainActivity, R.string.address_found, Toast.LENGTH_SHORT).show()
            }
            addressRequested = false
            updateUIWidgets()
        }
    }

    private fun showSnackbar(
            mainTextStringId: Int,
            actionStringId: Int,
            listener: View.OnClickListener
    ) {
        Snackbar.make(findViewById(android.R.id.content), getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener)
                .show()
    }

    private fun checkPermissions() {

        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                    ),
                    REQUEST_PERMISSIONS_REQUEST_CODE
            )
        } else {
            getAddress()
        }
    }

    private fun startIntentService() {
        val intent = Intent(this, GetAddressIntentService::class.java).apply {
            putExtra(Constants.RECEIVER, resultReceiver)
            putExtra(Constants.LOCATION_DATA_EXTRA, lastLocation)
        }
        startService(intent)
    }


    private fun getAddress() {
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                    ),
                    REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
        fusedLocationClient?.lastLocation?.addOnSuccessListener(this, OnSuccessListener { location ->
            if (location == null) {
                Log.i(TAG, "::::::onSuccess:null")
                return@OnSuccessListener
            }

            lastLocation = location

            val geocoder = Geocoder(this, Locale.getDefault())

            var addresses: List<Address> = emptyList()

            try {
                addresses = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                )
                cityAddress = addresses[0].locality

            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (lastLocation!!.longitude != null) {
                loadingIndicator.visibility = View.VISIBLE
                try {
                    mMainActivityViewModel.setIsQueryingWeather(true)
                    mMainActivityViewModel.searchWeatherApi(
                            lastLocation!!.latitude,
                            lastLocation!!.longitude, Constants.API_KEY, Constants.units
                    )
                } finally {
                    refreshButton.visibility = View.VISIBLE
                }
            }

            if (!Geocoder.isPresent()) {
                Snackbar.make(findViewById(android.R.id.content),
                        R.string.no_geocoder_available, Snackbar.LENGTH_LONG).show()
                return@OnSuccessListener
            }

            if (addressRequested) startIntentService()
        })?.addOnFailureListener(this) { e ->
            Log.i(TAG, "=====>>>>>getLastLocation:onFailure", e)
        }
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")

        if (requestCode != REQUEST_PERMISSIONS_REQUEST_CODE) return

        when {
            grantResults.isEmpty() ->
                Log.i(TAG, "User interaction was cancelled.")
            grantResults[0] == PackageManager.PERMISSION_GRANTED -> // Permission granted.
                getAddress()
            else -> // Permission denied.

                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        {
                            // Build intent that displays the App settings screen.
                            val intent = Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.fromParts("package", "com.michaeloki.climatewatch", null)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            startActivity(intent)
                        })
        }
    }


    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle(getString(R.string.app_name))

        builder.setMessage(getString(R.string.exitQuestion))

        builder.setPositiveButton(
                getString(R.string.YES_TEXT)
        ) { _, _ -> finish() }

        builder.setNegativeButton(
                getString(R.string.NO_TEXT)
        ) { _, _ ->
            Toast.makeText(
                    applicationContext, resources.getString(R.string.stayTuned),
                    Toast.LENGTH_SHORT
            ).show()
        }


        builder.setNeutralButton(getString(R.string.CANCEL_TEXT)) { _, _ ->
            Toast.makeText(
                    applicationContext, resources.getString(R.string.stayTuned),
                    Toast.LENGTH_SHORT
            ).show()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}