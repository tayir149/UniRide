package com.example.uniride.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_create_trip_confirmation.*

class CreateTripConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip_confirmation)

        //Receive information from create trip page
        val bundle: Bundle? = intent.extras
        val dateOfTrip = bundle!!.getString("Date of trip")
        val eta = bundle.getString("Arrival time")
        val route = bundle.getString("Route of trip")
        val price = bundle.getDouble("Price of trip")
        val carDetails = bundle.getString("Car details")
        val numberOfPassenger = bundle.getInt("Number of passenger")

        //Display the trip details on screen
        val tripDetails = "Date: $dateOfTrip\n" +
                "Arrival Time: $eta\n" +
                "Route: $route\n" +
                "Price: \$$price\n" +
                "Car make and model: $carDetails" +
                "Maximum number of passenger: $numberOfPassenger"
        tripConfirmationText.text = tripDetails


        backButton2.setOnClickListener {
            backToCreateTrip()

        }

        cancelButton.setOnClickListener {
            backToCreateTrip()
        }

        confirmButton.setOnClickListener {
            Toast.makeText(this, "Trip Created Successfully", Toast.LENGTH_SHORT).show()
        }
    }









    private fun backToCreateTrip() {
        val intent = Intent(this, CreateTrip::class.java)
        startActivity(intent)
    }
}
