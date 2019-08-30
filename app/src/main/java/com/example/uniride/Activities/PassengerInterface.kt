package com.example.uniride.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_passenger_interface.*

class PassengerInterface : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger_interface)

        driverPassengerSwitch.setOnCheckedChangeListener { _, _ ->
            val intent = Intent(this, DriverInterface::class.java)
            startActivity(intent)
        }
    }
}