package com.example.mp3test.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromArrayList(list: ArrayList<String>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toArrayList(jsonString: String?): ArrayList<String>? {
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(jsonString, type)
    }
}