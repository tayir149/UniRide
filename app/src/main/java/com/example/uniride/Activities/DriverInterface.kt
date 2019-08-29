package com.example.uniride.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_driver_interface.*

class DriverInterface : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_interface)

        //When CREATE TRIP button pressed, will be switched to create trip page
        createTripButton.setOnClickListener{
            val intent = Intent(this, CreateTrip::class.java)
            startActivity(intent)
        }









    }
}
