package com.mahesh.weather

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.mahesh.debug.weather.FragmentTestActivity
import com.mahesh.weather.forecast.ForecastFragment
import com.mahesh.weather.forecast.ForecastPresenter
import com.mahesh.weather.util.ViewModelProviderFactory
import com.squareup.picasso.Picasso
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class ForecastFragmentTest {

    private val fragment = ForecastFragment()

    private val presenter: ForecastPresenter = mock(ForecastPresenter::class.java)

    private val picasso: Picasso = mock(Picasso::class.java)

    @Rule
    @JvmField
    val activityTestRule = object : ActivityTestRule<FragmentTestActivity>(FragmentTestActivity::class.java) {
        override fun afterActivityLaunched() {
            runOnUiThread {
                activity.apply {
                    startFragment(fragment, this@ForecastFragmentTest::inject)
                }
            }
            super.afterActivityLaunched()
        }
    }

    private fun inject(fragment: ForecastFragment) {
        fragment.viewModelProvider = ViewModelProviderFactory(presenter)
        fragment.picasso = picasso
    }

    @Test
    fun setDate_shouldDisplayDate() {
        fragment.setDate("Date")
        onView(withId(R.id.tv_date)).check(matches(withText("Date")))
    }
}
