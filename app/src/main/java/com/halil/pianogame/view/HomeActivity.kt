package com.halil.pianogame.view

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.halil.pianogame.R
import com.halil.pianogame.sharedPreferences.MySharedPreferences
import com.halil.pianogame.util.BACKGROUND_MUSICPLAYER
import com.halil.pianogame.util.backgroundMediaPlayerLiveData

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        observeLiveData()
        var uri= MySharedPreferences(applicationContext).getBackGroundMusicUri()
       // uri=null

        if(backgroundMediaPlayerLiveData.value!=null) {
            backgroundMediaPlayerLiveData.value = if (uri != null) MediaPlayer.create(
                applicationContext,
                uri
            ) else BACKGROUND_MUSICPLAYER(applicationContext)
        }
         Log.e("HomeActivity,", "onCreate: "+ uri )
    }
    var backgroundMediaPlayer:MediaPlayer?=null

    fun observeLiveData(){

        backgroundMediaPlayerLiveData.observe(this, Observer {
            if(!it.isPlaying) {
                backgroundMediaPlayer?.stop()
                backgroundMediaPlayer?.release()
                backgroundMediaPlayer = it
                backgroundMediaPlayer?.isLooping = true
                mediaPlayer = backgroundMediaPlayer
            }
           // backgroundMediaPlayer.setVolume(0.09f,0.09f)
            backgroundMediaPlayer?.start()
        })
    }

    override fun onPause() {
        super.onPause()
        backgroundMediaPlayer?.pause()
    }
    override fun onStop() {
        super.onStop()



    }

    override fun onStart() {
        super.onStart()
        backgroundMediaPlayer?.start()
    }
companion object{
    var mediaPlayer:MediaPlayer?=null
}
}