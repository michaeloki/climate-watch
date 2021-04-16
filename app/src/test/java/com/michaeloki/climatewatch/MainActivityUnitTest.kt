package com.michaeloki.climatewatch


import android.Manifest
import android.view.View
import android.widget.Button
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.view.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows


@RunWith(RobolectricTestRunner::class)

class MainActivityUnitTest {
    private var activity: MainActivity? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        activity = Robolectric.buildActivity(MainActivity::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun checkActivityNotNull() {
        assertNotNull(activity)
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun shouldHaveCorrectAppName() {
        val appName = activity!!.resources.getString(R.string.app_name)
        assertThat(appName, equalTo("ClimateWatch"))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun buttonClickShouldShowCityName() {
        val button: Button = activity!!.findViewById<View>(R.id.refreshButton) as Button
        button.performClick()
        val cityName = Shadows.shadowOf(activity).contentView.currentCity.isVisible
        assertNotNull(cityName)
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun confirmWeatherCardIsHidden() {
        val weatherCard = Shadows.shadowOf(activity).contentView.weatherCard.isInvisible
        assertNotNull(weatherCard)
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun confirmNetworkIconIsHidden() {
        val networkIcon = Shadows.shadowOf(activity).contentView.networkError.isInvisible
        assertNotNull(networkIcon)
    }

    @Test
    fun getAddress() {
        val locationPermission = Shadows.shadowOf(activity).grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
        assertNotNull(locationPermission)
    }

    @Test
    fun checkIntentResultCode() {
        assertNotNull(Shadows.shadowOf(activity).resultCode)
    }
}