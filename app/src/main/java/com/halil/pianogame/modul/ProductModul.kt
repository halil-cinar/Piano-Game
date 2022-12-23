package com.halil.pianogame.modul

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductModul(
    @ColumnInfo
    var title:String,
    @ColumnInfo
    var price:Int,
    @ColumnInfo
    var ImageByteArray:ByteArray,
    @ColumnInfo
    var isTaken:Boolean,
    @ColumnInfo
    var isUse:Boolean=false,
    @ColumnInfo
    var data:String?,
    @ColumnInfo
    var typeId:Int,
    @ColumnInfo
    var uuid:Int,





){
   @PrimaryKey(autoGenerate = true)
   var id=0



}
