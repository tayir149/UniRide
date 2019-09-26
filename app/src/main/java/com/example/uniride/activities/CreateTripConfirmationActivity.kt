package com.example.uniride.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import com.example.uniride.showToast
import kotlinx.android.synthetic.main.activity_create_trip_confirmation.*
import org.jetbrains.anko.startActivity

class CreateTripConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip_confirmation)

        /* Receive information from create trip page */
        val bundle: Bundle? = intent.extras
        val dateOfTrip = bundle!!.getString("Date of trip")
        val eta = bundle.getString("Arrival time")
        val route = bundle.getString("Route of trip")
        val price = bundle.getDouble("Price of trip")
        val carDetails = bundle.getString("Car details")
        val numberOfPassenger = bundle.getInt("Number of passenger")
        val driverName = bundle.getString("Driver Name")
        val userEmail = bundle.getString("User Email")

        /* Display the trip details on screen */
        val tripDetails = "Driver`s Name: $driverName\n" +
                "Date: $dateOfTrip\n" +
                "Arrival Time: $eta\n" +
                "Route: $route\n" +
                "Price: \$$price\n" +
                "Car make and model: $carDetails\n" +
                "Maximum number of passenger: $numberOfPassenger"
        tripConfirmationText.text = tripDetails


        backButton2.setOnClickListener {
            finish()

        }

        cancelButton.setOnClickListener {
            finish()
        }

        confirmButton.setOnClickListener {
            val tripCreated = com.example.uniride.classes.Trip(driverName, dateOfTrip, eta, route, price, carDetails, numberOfPassenger, userEmail)
            tripCreated.saveTripToDatabase()
            showToast("Trip Created Successfully")
            Log.d("testCreatingTrip","Successfully created trip")
            startActivity<DriverInterface>()
        }
    }
}
