package com.halil.pianogame.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.halil.pianogame.modul.ImageModel

@Dao
interface Dao2 {
    @Insert(entity = ImageModel::class)
    suspend fun saveImageModel(vararg imageModel: ImageModel):List<Long>

    @Query(value = "SELECT * FROM imagemodel")
    suspend fun getAllImageModel():List<ImageModel>

    @Query(value = "DELETE FROM imagemodel")
    suspend fun deleteAllImageModel()

}