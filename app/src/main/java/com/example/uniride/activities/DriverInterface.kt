package com.example.uniride.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_driver_interface.*
import org.jetbrains.anko.startActivity

class DriverInterface : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_interface)

        /* When CREATE TRIP button pressed, will navigate to create trip page */
        createTripButton.setOnClickListener{
            startActivity<CreateTrip>()
        }

        /* Switch between Driver Interface and Passenger Interface */
        driverPassengerSwitch.setOnCheckedChangeListener { _, _ ->
            startActivity<PassengerInterface>()
        }

        /* When TRIP HISTORY button pressed, will navigate to history page */
        tripHistoryButton.setOnClickListener {
            startActivity<HistoryActivity>()
        }

        upcomingTripButton.setOnClickListener {
            startActivity<UpcomingActivity>()
        }
















    }
}
