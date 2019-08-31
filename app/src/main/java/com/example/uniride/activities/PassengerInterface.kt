package com.example.uniride.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_passenger_interface.*
import org.jetbrains.anko.startActivity

class PassengerInterface : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger_interface)

        /* Switch between Driver Interface and Passenger Interface */
        driverPassengerSwitch.setOnCheckedChangeListener { _, _ ->
            startActivity<DriverInterface>()
        }
    }
}