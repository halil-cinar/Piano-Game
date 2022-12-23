package com.halil.pianogame.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.halil.pianogame.modul.ImageModel

import com.halil.pianogame.modul.ProductModul

@Database(entities = [ProductModul::class,ImageModel::class], version = 2)
abstract class RoomDatabase:androidx.room.RoomDatabase() {
    abstract fun getDao():Dao
    abstract fun getImageDao():Dao2

companion object{
    @Volatile var instance:com.halil.pianogame.room.RoomDatabase?=null
    var lock=Any()
    fun createDatabase(context: Context): RoomDatabase {
     return   Room.databaseBuilder(context,RoomDatabase::class.java,"product").build()
    }

    operator fun invoke(context: Context)= instance?: synchronized(lock){
        instance ?: createDatabase(context).also {
            instance=it
        }
    }
}
}