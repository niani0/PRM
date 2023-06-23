package com.example.myapplication.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.myapplication.location.DefaultLocationClient
import com.example.myapplication.interfaces.LocationClient
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.LocationServices

class AlertService : Service() {



    companion object {
        private const val CHANNEL_ID = "alert_channel"
        private const val NOTIFICATION_ID = 1
    }

    private lateinit var notificationManager: NotificationManager
    private var id: String? = null
    private lateinit var locationClient: LocationClient

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            this,
            LocationServices.getFusedLocationProviderClient(this)
        )
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val geofence = intent?.let { GeofencingEvent.fromIntent(it) }
        id = geofence?.triggeringGeofences?.firstOrNull()?.requestId
        if (id != null) {
            sendNotification(id!!)
        }
        return START_NOT_STICKY
    }

    private fun sendNotification(geofenceId: String) {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Sale")
            .setContentText("$geofenceId w pobliżu!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationBuilder.setContentIntent(pendingIntent)

        createNotificationChannel()

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alert Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}

