package com.example.myapplication.location

import android.app.PendingIntent
import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.example.myapplication.service.AlertService

const val RANGE = 200.0
private const val REQUEST_CODE = 2
private const val ACTION_GEOFENCE_TRIGGER = "com.example.myapplication.ACTION_GEOFENCE_TRIGGER"
private const val EXTRA_GEOFENCE_ID = "com.example.myapplication.EXTRA_GEOFENCE_ID"


object Geofencing {


    fun createGeofence(context: Context, latLng: LatLng, name: String) {

        val geofence = Geofence.Builder()
            .setCircularRegion(latLng.latitude, latLng.longitude, RANGE.toFloat())
            .setRequestId(name)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()

        val request = GeofencingRequest.Builder()
            .addGeofence(geofence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()

        if (ContextCompat.checkSelfPermission(
                context,
                ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)
            ).let {
                context.startActivity(it)
            }
        } else {
            LocationServices.getGeofencingClient(context)
                .addGeofences(request, makePendingIntentForAlert(context, name))
                .addOnFailureListener { println(it) }
                .addOnSuccessListener {
                    println("Dodano Geofence " + latLng.latitude + " " + latLng.longitude)
                }
        }
    }


    private fun makePendingIntentForAlert(context: Context, geofenceId: String): PendingIntent {
        val serviceIntent = Intent(context, AlertService::class.java)
        serviceIntent.action = ACTION_GEOFENCE_TRIGGER
        serviceIntent.putExtra(EXTRA_GEOFENCE_ID, geofenceId)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(
                context,
                REQUEST_CODE,
                serviceIntent,
                FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getService(
                context,
                REQUEST_CODE,
                serviceIntent,
                FLAG_UPDATE_CURRENT
            )
        }
    }
    
}