package com.example.uniride.activities

import android.net.Uri
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R

import kotlinx.android.synthetic.main.activity_video_help.*

class VideoHelp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_help)

        var uriPath = "android.resource://"+packageName+"/"+R.raw.idhelp
        var uri = Uri.parse(uriPath)
        vv.setVideoURI(uri)
        vv.requestFocus()
        vv.start()
        vv.setOnCompletionListener {
            finish()
        }
    }
}
