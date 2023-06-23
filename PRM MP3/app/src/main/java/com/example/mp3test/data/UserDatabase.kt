package com.example.mp3test.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase(){
    abstract val users: UserDao

    companion object {
        fun open(context: Context): UserDatabase = Room.databaseBuilder(
            context, UserDatabase::class.java, "users.db"
        ).build()
    }
}