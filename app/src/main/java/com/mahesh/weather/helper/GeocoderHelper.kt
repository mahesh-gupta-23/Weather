package com.mahesh.weather.helper

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.mahesh.weather.R
import com.mahesh.weather.app.coroutines.CoroutinesManager
import com.mahesh.weather.app.coroutines.asynctaskmanager.AsyncTasksManager
import com.mahesh.weather.util.CustomAddress
import com.mahesh.weather.util.LatLng
import com.mahesh.weather.util.getCustomAddress
import javax.inject.Inject

class GeocoderHelper @Inject constructor(
    coroutinesManager: CoroutinesManager, private val geocoder: Geocoder,
    private val asyncTasksManager: AsyncTasksManager, private val context: Context
) : CoroutinesManager by coroutinesManager {

    fun getAddress(
        retryCount: Int = 0, latLng: LatLng,
        onAddressFetched: (customAddress: CustomAddress) -> Unit,
        onAddressError: (error: String) -> Unit
    ) {
        launchOnUITryCatch({
            val addressList = getAddressAsync(latLng)
            if (addressList.isEmpty()) {
                onAddressError.invoke(context.getString(R.string.no_address_found))
            } else {
                onAddressFetched.invoke(addressList[0].getCustomAddress())
            }
        }, {
            if (retryCount <= 2) {
                getAddress(retryCount + 1, latLng, onAddressFetched, onAddressError)
            }
        })
    }

    private suspend fun getAddressAsync(latLng: LatLng): List<Address> {
        return asyncTasksManager.asyncAwait {
            with(latLng) {
                geocoder.getFromLocation(latitude, longitude, 1)
            }
        }
    }
}