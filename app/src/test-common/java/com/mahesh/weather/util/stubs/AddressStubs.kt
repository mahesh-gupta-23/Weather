package com.mahesh.weather.util.stubs

import android.location.Address
import com.mahesh.weather.app.extensions.CustomAddress
import java.util.*

class AddressStubs {
    companion object {
        @JvmField
        val CURRENT_ADDRESS: Address = with(Address(Locale.ENGLISH)) {
            subAdminArea = "Mumbai Suburban"
            locality = "Mumbai"
            adminArea = "Maharashtra"
            return@with this
        }

        @JvmField
        val CURRENT_CUSTOM_ADDRESS: CustomAddress =
            CustomAddress(
                "Maharashtra", "Mumbai Suburban", "Mumbai", null,
                null, null, "India"
            )
    }
}