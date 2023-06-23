package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.data.model.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ProductDatabase : RoomDatabase() {
    abstract val products: ProductDao

    companion object {
        fun open(context: Context): ProductDatabase = Room.databaseBuilder(
            context, ProductDatabase::class.java, "products.db"
        ).build()
    }
}