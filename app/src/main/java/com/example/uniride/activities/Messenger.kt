package com.example.uniride.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R

class Messenger : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip_confirmation)

        val bundle: Bundle? = intent.extras
        val userID = bundle?.getString("userID")
        val intent = Intent()
        intent.putExtra(Intent.EXTRA_TEXT, "Hello")
        intent.type = "text/plain"
        intent.setPackage("com.facebook.orca")
        intent.data = Uri.parse("https://m.me/" + userID)

        try {
            startActivity(intent)
            Thread.sleep(2000)
            finish()
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "Can't open Facebook Messenger. Is it installed?",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
