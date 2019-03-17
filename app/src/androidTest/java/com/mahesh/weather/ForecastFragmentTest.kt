package com.mahesh.weather

import android.util.Log
import android.widget.ImageView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.mahesh.weather.app.TAG
import com.mahesh.weather.app.extensions.loadWeatherIn
import com.mahesh.weather.forecast.ForecastFragment
import com.mahesh.weather.forecast.ForecastPresenter
import com.mahesh.weather.testing.SingleFragmentActivity
import com.mahesh.weather.util.KotlinTestUtils.Companion.whenever
import com.mahesh.weather.util.ViewModelProviderFactory
import com.squareup.picasso.Picasso
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

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
    fun showSnackBar_shouldDisplaySnackbarWithAppropriateMessage() {
        val text = "SnackBar"
        forecastFragment.showSnackBar(text)
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(text)))
    }

    @Test
    fun showCurrentTemp_shouldDisplayCurrentTemp() {
        val text = 28.0
        forecastFragment.setCurrentTemp(text)
        onView(withId(R.id.tv_current_temp)).check(
            matches(
                withText(
                    forecastFragment.context?.getString(R.string.temp, text.toString())
                )
            )
        )
    }

    @Test
    fun hideWeatherImage_shouldHide() {
        forecastFragment.toggleWeatherImageVisibility(false)
        onView(withId(R.id.iv_weather)).check(matches(not(isDisplayed())))
    }

    @Test
    fun showWeatherImage_shouldShow() {
        forecastFragment.toggleWeatherImageVisibility(true)
        onView(withId(R.id.iv_weather)).check(matches(isDisplayed()))
    }

    fun loadWeatherImage_shouldCallPicassoLoadWithAppropriateData() {
        val iconName = "icon"
        val view: ImageView = forecastFragment.view?.findViewById(R.id.iv_weather) as ImageView
        Log.e(TAG, "view $view")
        whenever(picasso.loadWeatherIn(iconName, view)).then { }
        forecastFragment.loadWeatherImage(iconName)
        verify(picasso).loadWeatherIn(iconName, view)
    }
}
