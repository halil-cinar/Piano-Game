package com.halil.pianogame.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.halil.pianogame.modul.ProductModul

@Dao
interface Dao {
    @Insert(entity = ProductModul::class)
    suspend fun saveProductModel(vararg productModul: ProductModul):List<Long>

    @Query(value = "SELECT * FROM productmodul ")
   suspend fun getProductModelList():List<ProductModul>

    @Query(value = "DELETE FROM productmodul ")
    suspend fun deleteAllProductModel()


}

