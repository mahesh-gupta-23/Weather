package com.mahesh.weather

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.mahesh.weather.forecast.ForecastFragment
import com.mahesh.weather.forecast.ForecastPresenter
import com.mahesh.weather.testing.SingleFragmentActivity
import com.mahesh.weather.util.REQUEST_CHECK_SETTINGS
import com.mahesh.weather.util.REQUEST_PERMISSION_CODE
import com.mahesh.weather.util.ViewModelProviderFactory
import com.squareup.picasso.Picasso
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class ForecastFragmentTest {

    private val forecastFragment = ForecastFragment()

    private val presenter: ForecastPresenter = mock(ForecastPresenter::class.java)

    private val picasso: Picasso = mock(Picasso::class.java)

    @Rule
    @JvmField
    val activityTestRule = object : ActivityTestRule<SingleFragmentActivity>(SingleFragmentActivity::class.java) {
        override fun afterActivityLaunched() {
            runOnUiThread {
                activity.apply {
                    startFragment(forecastFragment, this@ForecastFragmentTest::inject)
                }
            }
            super.afterActivityLaunched()
        }
    }

    private fun inject(forecastFragment: ForecastFragment) {
        forecastFragment.viewModelProvider = ViewModelProviderFactory(presenter)
        forecastFragment.picasso = picasso
    }

    @Before
    fun init() {
        forecastFragment.toggleProgressBar(false)
    }

    @Test
    fun setDate_shouldDisplayDate() {
        val text = "Date"
        forecastFragment.setDate(text)
        onView(withId(R.id.tv_date)).check(matches(withText(text)))
    }

    @Test
    fun setLocation_shouldDisplayLocation() {
        val text = "Location"
        forecastFragment.setLocation(text)
        onView(withId(R.id.tv_location)).check(matches(withText(text)))
    }

    @Test
    fun showCurrentTemp_shouldDisplayCurrentTemp() {
        val text = 28.0
        forecastFragment.setCurrentTemp(text)
        onView(withId(R.id.tv_current_temp)).check(
            matches(withText(forecastFragment.context?.getString(R.string.temp, text.toString())))
        )
    }

    @Test
    fun showWeatherImage_shouldShow() {
        forecastFragment.toggleWeatherImageVisibility(true)
        onView(withId(R.id.iv_weather)).check(matches(isDisplayed()))
    }

    @Test
    fun hideWeatherImage_shouldHide() {
        forecastFragment.toggleWeatherImageVisibility(false)
        onView(withId(R.id.iv_weather)).check(matches(not(isDisplayed())))
    }

    @Test
    fun showProgressBar_shouldShow() {
        forecastFragment.toggleProgressBar(true)
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }

    @Test
    fun hideProgressBar_shouldHide() {
        forecastFragment.toggleProgressBar(false)
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun setHumidity_shouldDisplayInProperFormat() {
        val humidity = 10
        forecastFragment.setHumidity(humidity)
        onView(withId(R.id.tv_humidity)).check(
            matches(withText(forecastFragment.context?.getString(R.string.relative_humidity, humidity, "%")))
        )
    }

    @Test
    fun showError_shouldDisplaySnackbarWithErrorAndButton() {
        val error = "Error on SnackBar"
        val context = forecastFragment.context
        forecastFragment.showErrorMessage(error)

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(context?.getString(R.string.exception_toast_message, error))))
        onView(withId(com.google.android.material.R.id.snackbar_action))
            .check(matches(withText(context?.getString(R.string.ok))))
    }

    @Test
    fun closeApplication_shouldFinishActivity() {
        forecastFragment.closeApplication()
        assert(activityTestRule.activity.isFinishing)
    }

    @Test
    fun showLocationPermissionNeeded_shouldDisplayDialog() {
        val titleId = activityTestRule.activity.resources.getIdentifier("alertTitle", "id", "android")
        val context = forecastFragment.context
        val onOk: () -> Unit = {}
        val onCancel: () -> Unit = {}

        forecastFragment.activity?.runOnUiThread {
            forecastFragment.showNeedLocationPermissionDialogToContinue(onOk, onCancel)
        }

        onView(withId(titleId))
            .inRoot(isDialog())
            .check(matches(withText(context?.getString(R.string.location_permission_required_title))))
            .check(matches(isDisplayed()))

        onView(withId(android.R.id.message))
            .inRoot(isDialog())
            .check(matches(withText(context?.getString(R.string.location_permission_required_content))))
            .check(matches(isDisplayed()))

        onView(withId(android.R.id.button1))
            .inRoot(isDialog())
            .check(matches(withText(context?.getString(android.R.string.ok))))
            .check(matches(isDisplayed()))

        onView(withId(android.R.id.button2))
            .inRoot(isDialog())
            .check(matches(withText(context?.getString(android.R.string.cancel))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun showLocationToBeEnabled_shouldDisplayDialog() {
        val titleId = activityTestRule.activity.resources.getIdentifier("alertTitle", "id", "android")
        val context = forecastFragment.context
        val onOk: () -> Unit = {}
        val onCancel: () -> Unit = {}

        forecastFragment.activity?.runOnUiThread {
            forecastFragment.showNeedLocationToBeEnabledToContinue(onOk, onCancel)
        }

        onView(withId(titleId))
            .inRoot(isDialog())
            .check(matches(withText(context?.getString(R.string.location_to_be_enabled_title))))
            .check(matches(isDisplayed()))

        onView(withId(android.R.id.message))
            .inRoot(isDialog())
            .check(matches(withText(context?.getString(R.string.location_to_be_enabled_content))))
            .check(matches(isDisplayed()))

        onView(withId(android.R.id.button1))
            .inRoot(isDialog())
            .check(matches(withText(context?.getString(android.R.string.ok))))
            .check(matches(isDisplayed()))

        onView(withId(android.R.id.button2))
            .inRoot(isDialog())
            .check(matches(withText(context?.getString(android.R.string.cancel))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun onRequestPermissionResult_shouldReturnTheResultToPresenter() {
        val requestCode = 1
        val permissions = arrayOf<String>()
        val grantResults = IntArray(0)
        forecastFragment.onRequestPermissionsResult(requestCode, permissions, grantResults)

        verify(presenter).onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Test
    fun onActivityResult_shouldReturnTheResultToPresenterOnlyIfItIsOfTypeCheckSetting() {
        val requestCode = REQUEST_CHECK_SETTINGS
        val resultCode = 1
        val data: Intent? = null

        forecastFragment.onActivityResult(requestCode, resultCode, data)
        verify(presenter).onActivityResult(requestCode, resultCode, data)

        forecastFragment.onActivityResult(REQUEST_PERMISSION_CODE, resultCode, data)
        verify(presenter, never()).onActivityResult(REQUEST_PERMISSION_CODE, resultCode, data)
    }
}
