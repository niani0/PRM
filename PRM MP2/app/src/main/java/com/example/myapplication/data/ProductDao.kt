package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.model.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun getAll() : List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProduct(newProductEntity: ProductEntity)

    @Query("SELECT * FROM product WHERE id=:id")
    fun getProduct(id: Int) : ProductEntity

    @Update
    fun updateProduct(newProductEntity: ProductEntity)

    @Query("DELETE FROM product WHERE id =:id")
    fun remove(id: Int)
}