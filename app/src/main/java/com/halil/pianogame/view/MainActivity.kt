package com.halil.pianogame.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.halil.pianogame.R
import com.halil.pianogame.util.backgroundMediaPlayerLiveData

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }

    override fun onPause() {
        super.onPause()
        finish()
        startActivity(Intent(this,HomeActivity::class.java))
        var mediaPlayer= backgroundMediaPlayerLiveData.value
        mediaPlayer?.pause()
    }

    override fun onStop() {
        super.onStop()

       //HomeActivity.mediaPlayer?.pause()

    }

    override fun onStart() {
        super.onStart()
        var mediaPlayer= backgroundMediaPlayerLiveData.value
mediaPlayer?.isLooping=true
        mediaPlayer?.start()
       // HomeActivity.mediaPlayer?.start()
    }

}