package com.halil.pianogame.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toFile
import com.halil.pianogame.R
import com.halil.pianogame.modul.ImageModel
import com.halil.pianogame.modul.ProductModul
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList


const val COLUMN_COUNT=3
//column to row

val ROW_COUNT= hashMapOf(1 to 2 , 2 to 2 , 3 to 2)
    val TAG ="TAGG"

val LEVEL_RATE=40
var uuid=R.drawable.background_2.toString()
fun getProductList(context: Context): ArrayList<ProductModul> {
    var list= arrayListOf<ProductModul>()
    var stream=ByteArrayOutputStream()//low quality


    resizeImage(BitmapFactory.decodeResource(context.resources, R.drawable.background),300).compress(Bitmap.CompressFormat.PNG,0,stream)

    list.add(ProductModul("BackGround",0,stream.toByteArray(),true,true,uuid,1,-1))
    stream.reset()

    var image=BitmapFactory.decodeResource(context.resources,R.drawable.play)
    image.compress(Bitmap.CompressFormat.PNG,0,stream)
    //var file=MediaStore.Images.Media.insertImage(context.contentResolver, image, "play", null)
    var file= Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(context.packageName)
        .appendPath("${R.raw.background}")
        .build()
    println(file)
    val fileurl=file

    list.add(ProductModul("BackGround Music",0,stream.toByteArray(),true,true,fileurl.toString(),2,-2))




    return list
}
fun getImageList(context: Context): ArrayList<ImageModel> {
    var stream2=ByteArrayOutputStream()//high quality
    resizeImage(BitmapFactory.decodeResource(context.resources, R.drawable.background_2),2000).compress(Bitmap.CompressFormat.PNG,0,stream2)
    //BitmapFactory.decodeResource(context.resources, R.drawable.background_2).compress(Bitmap.CompressFormat.PNG,0,stream2)

    var list= arrayListOf<ImageModel>()
    list.add(ImageModel(uuid, Gson().toJson(stream2.toByteArray())))
    return list
}
fun BACKGROUND_MUSICPLAYER(context: Context)=MediaPlayer.create(context,R.raw.background)
