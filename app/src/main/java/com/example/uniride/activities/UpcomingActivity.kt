package com.example.uniride.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uniride.R
import com.example.uniride.classes.Trip
import com.example.uniride.classes.TripsAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_upcoming.*
import org.jetbrains.anko.startActivity

class UpcomingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val tripArray = ArrayList<Trip>()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        val adapter = TripsAdapter(this, tripArray)
        recyclerView.adapter = adapter

        upcomingTripBackButton.setOnClickListener {
            finish()
        }

        db.collection("trips")
            .addSnapshotListener{value, e->
                if (e!=null){
                    Log.w("TripList", "Listen failed.", e)
                    return@addSnapshotListener
                }

                for (document in value!!.documentChanges) {
                    if(document.type == DocumentChange.Type.ADDED){
                        var driverName = document.document.getString("trip_driver")
                        var date = document.document.getString("date")
                        var route = document.document.getString("route")
                        var eta = document.document.getString("estimated_arrival_time")
                        var details = document.document.getString("car_detail")
                        var passengers = document.document.getLong("number_of_passengers")?.toInt()
                        var price = document.document.getLong("price")!!.toDouble()
                        var driverEmail = document.document.getString("user_email")
                        tripArray.add(Trip(driverName, date, eta, route, price, details, passengers, driverEmail))

                        adapter.notifyDataSetChanged()
                        Log.d("TripList", "test $tripArray")
                    }
                }
            }
    }
}
