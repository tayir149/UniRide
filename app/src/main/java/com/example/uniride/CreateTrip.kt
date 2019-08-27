package com.example.uniride

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_trip.*

class CreateTrip : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)

        //When back button pressed, page will go back to driver interface
        backButton.setOnClickListener{
            val intent = Intent(this, DriverInterface::class.java)
            startActivity(intent)
        }
    }
}
