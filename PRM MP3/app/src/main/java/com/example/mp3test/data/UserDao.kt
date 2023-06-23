package com.example.mp3test.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    fun addUser(user: UserEntity)

    @Query("SELECT * FROM user")
    fun getAll(): List<UserEntity>

    @Query("SELECT * FROM user WHERE login = :login")
    fun getUserByLogin(login: String): UserEntity?

    @Update
    fun updateUser(user: UserEntity)

}