package com.mahesh.weather.helper

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.mahesh.weather.R
import com.mahesh.weather.app.coroutines.CoroutinesManager
import com.mahesh.weather.app.coroutines.asynctaskmanager.AsyncTasksManager
import javax.inject.Inject

class GeocoderHelper @Inject constructor(
    coroutinesManager: CoroutinesManager,
    private val geocoder: Geocoder,
    private val asyncTasksManager: AsyncTasksManager,
    private val context: Context
) : CoroutinesManager by coroutinesManager {

    fun getAddress(
        retryCount: Int = 0,
        location: Location,
        onAddressFetched: (address: Address) -> Unit,
        onAddressError: (error: String) -> Unit
    ) {
        launchOnUITryCatch({
            val addressList = getAddressAsync(location)
            if (addressList.isEmpty()) {
                onAddressError.invoke(context.getString(R.string.no_address_found))
            } else {
                onAddressFetched.invoke(addressList[0])
            }
        }, {
            if (retryCount <= 2) {
                getAddress(retryCount + 1, location, onAddressFetched, onAddressError)
            }
        })
    }

    private suspend fun getAddressAsync(
        location: Location
    ): List<Address> {
        return asyncTasksManager.asyncAwait {
            geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
        }
    }
}