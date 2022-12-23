package com.halil.pianogame.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.halil.pianogame.modul.ImageModel
import com.halil.pianogame.modul.ProductModul
import com.google.gson.Gson

class MySharedPreferences {

    companion object{
        val BACKGROUND_KEY="background"
        val BACKGROUND_IMAGEKEY="background"
        val CASH_KEY="cash"
        val BACKGROUND_MUSIC_KEY="backgroundmusic"

        private var sharedPreferences:SharedPreferences?=null
        @Volatile private var instance:MySharedPreferences?=null
        private var lock=Any()
        operator fun invoke(context: Context)= instance?: synchronized(lock){
            instance ?: createPreferences(context).also {
                instance=it
            }
        }
       private fun createPreferences(context: Context):MySharedPreferences{
            sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context)
            return MySharedPreferences()
        }
    }

    fun saveUsedChanges(usedChange:ProductModul,key:String,imageModel: ImageModel?,imageKey:String?){

        sharedPreferences?.edit(commit = true){
            remove(key)
            putString(key, Gson().toJson(usedChange))
            if (imageModel!=null){
                remove(imageKey)
                putString(imageKey,Gson().toJson(imageModel))

            }
        }

    }
    fun getUsedChanges(key: String):ProductModul?{
        if (sharedPreferences!=null){
            var str= sharedPreferences!!.getString(key,null)
            return Gson().fromJson<ProductModul>(str,ProductModul::class.java)
        }
        else{
            return null
        }
    }
    fun getUsedImageModel(key: String):ImageModel?{
        if (sharedPreferences!=null){
            var str= sharedPreferences!!.getString(key,null)
            return Gson().fromJson<ImageModel>(str,ImageModel::class.java)
        }
        else{
            return null
        }
    }

    fun saveCash(cash:Int){
        sharedPreferences?.edit(commit = true){
            putInt(CASH_KEY,cash)
        }
    }

    fun getCash():Int?{
        if (sharedPreferences!=null){
            return sharedPreferences!!.getInt(CASH_KEY,100)
        }else{
            return null
        }
    }
    fun saveBackGroundMusicUri(uri:Uri){
        sharedPreferences?.edit(commit = true){
            putString(BACKGROUND_MUSIC_KEY,uri.toString())
        }
    }
    fun getBackGroundMusicUri():Uri?{

            var uriStr=sharedPreferences?.getString(BACKGROUND_MUSIC_KEY,null)
        Log.e("application", "onCreateakmk: "+uriStr )
            if(uriStr!=null){
                return Uri.parse(uriStr)
            }else{
                return null
            }

    }

}