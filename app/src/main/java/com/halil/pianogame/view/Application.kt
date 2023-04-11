package com.halil.pianogame.view

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.halil.pianogame.sharedPreferences.MySharedPreferences
import com.halil.pianogame.util.BACKGROUND_MUSICPLAYER
import com.halil.pianogame.util.backgroundMediaPlayerLiveData

class Application(): Application() {
    override fun onCreate() {
        super.onCreate()

        var uri=MySharedPreferences(applicationContext).getBackGroundMusicUri()

       // backgroundMediaPlayerLiveData.value = if (uri!=null)  MediaPlayer.create(applicationContext,uri) else BACKGROUND_MUSICPLAYER(applicationContext)
        backgroundMediaPlayerLiveData.value= BACKGROUND_MUSICPLAYER(applicationContext)
        var backgroundMediaPlayer =
            backgroundMediaPlayerLiveData.value ?: BACKGROUND_MUSICPLAYER(applicationContext)
        backgroundMediaPlayer.isLooping = true

       // backgroundMediaPlayer.setVolume(0.09f, 0.09f)
        //backgroundMediaPlayer.start()


    }
    override fun onTerminate() {
        super.onTerminate()
        backgroundMediaPlayerLiveData.value?.stop()
    }
}