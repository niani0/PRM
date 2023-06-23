package com.example.mp3test.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mp3test.converters.Converters

@Entity(tableName = "user")
@TypeConverters(Converters::class)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val login: String,
    @ColumnInfo(name = "article_titles")
    val articleTitles: ArrayList<String>
)