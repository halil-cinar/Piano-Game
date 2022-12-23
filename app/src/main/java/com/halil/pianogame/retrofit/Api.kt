package com.halil.pianogame.retrofit
import android.provider.MediaStore
import com.halil.pianogame.modul.ImageModel
import com.halil.pianogame.modul.ProductModelRetrofit

import retrofit2.Response
import retrofit2.http.GET
import java.io.File

interface Api {
    //Baseurl -> https://raw.githubusercontent.com/
//halil1260/PianoGameDataRepository/main/data.json
    @GET("halil1260/PianoGameDataRepository/main/data.json")
    suspend fun getData():Response<List<ImageModel>>

    @GET("halil1260/PianoGameDataRepository/main/ProductData.json")
    suspend fun getProductData():Response<List<ProductModelRetrofit>>




}