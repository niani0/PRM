package com.example.myapplication.data

import android.location.Geocoder
import android.net.Uri
import android.os.Build
import com.google.android.gms.maps.model.LatLng

object DataSource {
    var savedImageUri: Uri? = null
    var id:Int = 0
    var dbId:Int = -1
    var saved: Boolean = false
    var location: LatLng? = null

    @Suppress("DEPRECATION")
    fun Geocoder.getAddress(
        latitude: Double,
        longitude: Double,
        addressCallback: (country: String?, city: String?, street: String?) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getFromLocation(latitude, longitude, 1) { addresses ->
                val address = addresses.firstOrNull()
                val country = address?.countryName ?: ""
                val city = address?.locality ?: ""
                val street = address?.thoroughfare ?: ""
                addressCallback(country, city, street)
            }
            return
        }
        try {
            val addresses = getFromLocation(latitude, longitude, 1)
            val address = addresses?.firstOrNull()
            val country = address?.countryName ?: ""
            val city = address?.locality ?: ""
            val street = address?.thoroughfare ?: ""
            addressCallback(country, city, street)
        } catch (e: Exception) {
            addressCallback(null, null, null)
        }
    }

}