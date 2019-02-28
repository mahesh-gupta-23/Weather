package com.mahesh.weather.forecast

import androidx.lifecycle.Lifecycle
import com.mahesh.weather.app.coroutines.CoroutinesManager
import com.mahesh.weather.app.coroutines.TestCoroutinesManager
import com.mahesh.weather.helper.GeocoderHelper
import com.mahesh.weather.helper.LocationHelper
import com.mahesh.weather.helper.PermissionHelper
import com.mahesh.weather.testutils.BaseTest
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ForecastPresenterTest : BaseTest() {

    @Spy
    private var coroutinesManager: CoroutinesManager = TestCoroutinesManager()
    @Mock
    private lateinit var mockView: ForecastContract.View
    @Mock
    private lateinit var locationHelper: LocationHelper
    @Mock
    private lateinit var permissionHelper: PermissionHelper
    @Mock
    private lateinit var geocoderHelper: GeocoderHelper
    @Mock
    private lateinit var modelInteractor: ForecastContract.ModelInteractor

    @InjectMocks
    private lateinit var subject: ForecastPresenter

    @Before
    fun before() {
        subject.attachView(view = mockView, viewLifecycle = mock(Lifecycle::class.java))
    }
}