package com.halil.pianogame.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.halil.pianogame.R
import com.halil.pianogame.modul.ButtonVisibility
import com.google.gson.Gson
import kotlin.math.sqrt


@BindingAdapter("changeVisibility.list","changeVisibility.column","changeVisibility.row")
fun changeButtonVisibility(view: View, list: String?, column:Int, row:Int) {



    if (list != null) {


        var list = Gson().fromJson(list, Array<ButtonVisibility>::class.java)
        list.forEach {

            if (it.buttonColumn == column) {
                if (it.buttonRow == row) {

                    view.visibility = it.visibility
                    view.setPadding(0, it.padding, 0, 0)

                }
            }
        }
    }
}

@BindingAdapter("changeHeart.hearts","changeHeart.whichHeart")
fun changeHeart(view:ImageView,hearts: Int?,whichHeart:Int){
    hearts?.let {
       view.setImageResource( if (hearts>=whichHeart) R.drawable.heart_filled else R.drawable.heart)
    }

}

@BindingAdapter("visibilityChange.isVisible")
fun visibilityChange(view: View,visible:Boolean?){
    view.visibility=if(visible==true) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("changeBackgroundWithBitmap.bitmap")
fun changeBackgroundWithBitmap(view:View,bitmap: Bitmap?){
    bitmap?.let {

        view.background= BitmapDrawable(it).current
    }
}
@BindingAdapter("changeBackgroundWithByteArray.byteArray")
fun changeBackgroundWithByteArray(view: View,byteArray: ByteArray){
   var bitmap= BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    view.background=BitmapDrawable(bitmap).current

}

public fun resizeImage(bitmap: Bitmap,maxHeight:Int):Bitmap {

        var width: Double = bitmap.width.toDouble()
        var height: Double = bitmap.height.toDouble()
        var oran: Double = maxHeight / height
        var hipo: Double = sqrt(Math.pow(width, 2.0) + Math.pow(height, 2.0))
        width = sqrt(Math.pow(hipo * oran, 2.0) - Math.pow(maxHeight.toDouble(), 2.0))
        height = maxHeight.toDouble()

        return Bitmap.createScaledBitmap(bitmap, width.toInt(), height.toInt(), true)

}



