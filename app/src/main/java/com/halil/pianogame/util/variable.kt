package com.halil.pianogame.util

import android.graphics.Bitmap
import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData

var cashLiveData=MutableLiveData<Int>(100)
var backgroundImageLiveData=MutableLiveData<Bitmap>()
var backgroundMediaPlayerLiveData=MutableLiveData<MediaPlayer>()

