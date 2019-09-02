package com.example.uniride.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_passenger_driver_selector.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class PassengerDriverSelector: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger_driver_selector)

        passenger_button.setOnClickListener {
            val intent = Intent(this, PassengerInterface::class.java)
            startActivity(intent)
        }

        driver_button.setOnClickListener {
            val intent = Intent(this, DriverInterface::class.java)
            startActivity(intent)
        }
    }
}