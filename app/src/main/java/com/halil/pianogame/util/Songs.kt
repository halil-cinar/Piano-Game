package com.halil.pianogame.util

import android.media.MediaPlayer

class Songs {

    companion object{

    }

    private fun random(min:Int,max:Int): Long {
        return (System.nanoTime()+min)%max
    }

    fun createRandomSongs(size:Int,noteList:HashMap<String,MediaPlayer>): ArrayList<String> {
        var note= arrayListOf<String>()
        var i=0
        var keys= noteList.keys.toTypedArray()
        while (i<size) {
            var j = 0
            var noteCount = random(1, 3)
            var str=""
            while (j < noteCount) {
                val key = keys[random(0, keys.size).toInt()]
                str+=key
                str+="-"

                j += 1
            }
            if (str!="") {
                note.add(str)
                i += 1
            }

        }
        this.note.clear()
        this.note.addAll(note)
        return note
    }
    var note= arrayListOf<String>()
}