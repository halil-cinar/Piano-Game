package com.halil.pianogame.listener

import android.view.View

interface ClickListener {
    fun click(view: View,list: String,column:Int,row:Int)

    fun buttonClick(view: View)
}