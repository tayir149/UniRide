package com.example.uniride.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uniride.R
import com.example.uniride.classes.BookedTripAdapter
import com.example.uniride.classes.Trip
import com.example.uniride.classes.UpcomingTripsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_booked_trips.*
import kotlinx.android.synthetic.main.activity_upcoming.*
import kotlinx.android.synthetic.main.activity_upcoming.recyclerView

class BookedTrips : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booked_trips)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        val tripArray = ArrayList<Trip>()
        val uIdArray = ArrayList<String>()

        //Define a layout manager for recycler view
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        //Link the recycler view with the costume adapter and pass in the Trip array as data
        val adapter = BookedTripAdapter(this, tripArray, uIdArray)
        recyclerView.adapter = adapter

        val userProfileRef = currentUserEmail?.let { it1 ->
            db.collection("users").document(it1) }

        lateinit var passengerAddress: String
        userProfileRef?.get()?.addOnSuccessListener { document ->
            if(document != null){
                passengerAddress = document.getString("address").toString()

                db.collection("trips").whereArrayContains("passenger_list", passengerAddress)
                    .addSnapshotListener { value, e ->
                        if (e != null) {
                            Log.w("TripList", "Listen failed.", e)
                            return@addSnapshotListener
                        }

                        for (document in value!!.documentChanges) {

                            val driverName = document.document.getString("trip_driver")
                            val date = document.document.getString("date")
                            val route = document.document.getString("route")
                            val eta = document.document.getString("estimated_arrival_time")
                            val details = document.document.getString("car_detail")
                            val passengers = document.document.getLong("number_of_passengers")?.toInt()
                            val price = document.document.getDouble("price")
                            val driverEmail = document.document.getString("user_email")
                            val passengerList =document.document.get("passenger_list") as ArrayList<String>?

                            //Saves the document Uid reference for canceling and finishing trips
                            val uId = document.document.id

                            tripArray.add(Trip(driverName, date, eta, route, price, details, passengers, driverEmail, passengerList))
                            uIdArray.add(uId)
                            adapter.notifyDataSetChanged()
                        }
                    }
            } else{
                Log.d("Error", "No such document")
            }
        }?.addOnFailureListener { exception ->
            Log.d("Main", "get failed with ", exception)
        }


        bookedTripBackButton.setOnClickListener {
            val intent = Intent(this, PassengerInterface::class.java)
            startActivity(intent)
        }
    }
}
