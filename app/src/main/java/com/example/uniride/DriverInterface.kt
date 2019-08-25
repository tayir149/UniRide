package com.example.uniride

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_driver_interface.*

class DriverInterface : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_interface)

        createTripButton.setOnClickListener{
            val intent = Intent(this, CreateTrip::class.java)
            startActivity(intent)
        }









    }
}
