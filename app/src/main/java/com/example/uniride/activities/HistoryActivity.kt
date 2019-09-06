package com.example.uniride.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        button6.setOnClickListener {
            openMessenger("royalty37")
        }
    }

    fun openMessenger(fbUserId: String)
    {
        val intent = Intent()
        intent.putExtra(Intent.EXTRA_TEXT, "Hello")
        intent.type = "text/plain"
        intent.setPackage("com.facebook.orca")
        intent.data = Uri.parse("https://m.me/"+fbUserId)

        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this,"Can't open Facebook Messenger. Is it installed?", Toast.LENGTH_SHORT).show()
        }
    }
}
