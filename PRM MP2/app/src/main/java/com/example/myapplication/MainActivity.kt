package com.example.myapplication

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.myapplication.interfaces.Navigable.Destination.*
import com.example.myapplication.fragments.*
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import com.example.myapplication.interfaces.Navigable

const val CHANNEL_ID = "alert_channel"



class MainActivity : AppCompatActivity(), Navigable {
    private lateinit var listFragment: ListFragment
    private lateinit var paintFragment: PaintFragment
    private lateinit var mapsFragment: MapsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissions()
        createNotificationChannel()
        listFragment = ListFragment()
        paintFragment = PaintFragment()
        mapsFragment = MapsFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, listFragment, listFragment.javaClass.name)
            .commit()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alert Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun permissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION

                ),
                0
            )
        }
    }


    override fun navigate(to: Navigable.Destination, id: Int?  ) {
        supportFragmentManager.beginTransaction().apply {
            when (to) {
                List -> {
                    replace(R.id.container, listFragment, listFragment.javaClass.name)
                    addToBackStack(listFragment.javaClass.name)
                }
                Add -> {
                    replace(
                        R.id.container,
                        EditFragment::class.java,
                        Bundle().apply { putInt(ARG_EDIT_ID, id ?: -1) },
                        EditFragment::class.java.name
                    )
                    addToBackStack(EditFragment::class.java.name)
                }
                Edit -> {
                    replace(
                        R.id.container,
                        EditFragment::class.java,
                        Bundle().apply { putInt(ARG_EDIT_ID, id ?: -1) },
                        EditFragment::class.java.name
                    )

                    addToBackStack(EditFragment::class.java.name)
                }
                Paint -> {
                    replace(R.id.container, paintFragment, PaintFragment::class.java.name)
                    addToBackStack(PaintFragment::class.java.name)
                }
                Map -> {
                    replace(R.id.container, mapsFragment, MapsFragment::class.java.name)
                    addToBackStack(MapsFragment::class.java.name)
                }
            }.commit()
        }
    }

}