package com.example.myapplication.data

import android.net.Uri
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromUriToString(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun fromStringToUri(string: String?): Uri? {
        return string?.let { Uri.parse(it) }
    }
}