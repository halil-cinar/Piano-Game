package com.halil.pianogame.modul

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageModel(
    @ColumnInfo
    var uuid: String,
    @ColumnInfo
    var image:String,
    @ColumnInfo
    var data:String?=null
){
    @PrimaryKey(autoGenerate = true)
    var id=0
}
