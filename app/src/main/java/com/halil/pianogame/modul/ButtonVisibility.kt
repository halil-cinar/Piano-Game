package com.halil.pianogame.modul

import androidx.annotation.IdRes

data class ButtonVisibility(
    var buttonColumn:Int,
    var buttonRow:Int,
    var visibility: Int,
    var padding: Int,
    @IdRes
    var id:Int?=null


) {
    var running: Boolean = false
    fun setrunning(running:Boolean) :ButtonVisibility{
       this.running=running
        return this
    }
}