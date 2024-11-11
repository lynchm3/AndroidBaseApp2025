package com.marklynch.steamdeck.data.buttons

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ButtonsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: StreamDeckButton)

    @Query("SELECT * FROM buttons")
    suspend fun getAll(): List<StreamDeckButton>

    @Delete
    suspend fun delete(user: StreamDeckButton)
}