package com.example.uniride.activities

import android.content.Intent
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

        universityToHomeButton.setOnClickListener{
            val intent = Intent(this, TripSearcher::class.java)
            intent.putExtra("FROM_ACTIVITY","uni-home")
            startActivity(intent)
        }
        homeToUniversityButton.setOnClickListener{
            val intent = Intent(this, TripSearcher::class.java)
            intent.putExtra("FROM_ACTIVITY","home-uni")
            startActivity(intent)
        }

        /* When TRIP HISTORY button pressed, will navigate to history page */
        tripHistoryButton.setOnClickListener {
            startActivity<HistoryActivity>()
        }
    }
}