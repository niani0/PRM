package com.example.myapplication.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val loc: String,
    val info: String,
    val icon: Uri,
    val lat: Double,
    val len: Double
)
