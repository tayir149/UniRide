package com.example.uniride.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uniride.R
import com.example.uniride.classes.Trip
import com.example.uniride.classes.UpcomingTripsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_upcoming.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UpcomingActivity : AppCompatActivity() {

    @Suppress("UNCHECKED_CAST", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val tripArray = ArrayList<Trip>()
        val uIdArray = ArrayList<String>()


        //Define a layout manager for recycler view
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        //Link the recycler view with the costume adapter and pass in the Trip array as data
        val adapter = UpcomingTripsAdapter(this, tripArray, uIdArray)
        recyclerView.adapter = adapter


        //Only gets current user created trips and only gets future trips
        val cal = Calendar.getInstance()
        val nowDate = dateFormat.format(cal.time)
        val nowTime = timeFormat.format(cal.time)

        db.collection("trips").whereEqualTo("user_email",
            FirebaseAuth.getInstance().currentUser?.email
        ).orderBy("date").orderBy("estimated_arrival_time").addSnapshotListener{value, e->
                if (e!=null){
                    Log.w("TripList", "Listen failed.", e)
                    return@addSnapshotListener
                }

                for (document in value!!.documentChanges) {
                    if(document.type == DocumentChange.Type.ADDED){
                        val driverName = document.document.getString("trip_driver")
                        val date = document.document.getString("date")
                        val route = document.document.getString("route")
                        val eta = document.document.getString("estimated_arrival_time")
                        val details = document.document.getString("car_detail")
                        val passengers = document.document.getLong("number_of_passengers")?.toInt()
                        val price = document.document.getDouble("price")
                        val driverEmail = document.document.getString("user_email")
                        val passengerList =document.document.get("passenger_list") as ArrayList<String>?

                        //In order to correctly compare time and date, it needs to be Date Object
                        val dateFormatted = dateFormat.parse(date)
                        val currentDate = dateFormat.parse(nowDate)
                        val timeFromDataBase = timeFormat.parse(eta)
                        val currentTime = timeFormat.parse(nowTime)

                        //Saves the document Uid reference for editing and deleting trips
                        val uId = document.document.id

                        //Filters the trips only show future trips
                        if ((dateFormatted.compareTo(currentDate) == 0 && timeFromDataBase >= currentTime) || dateFormatted.compareTo(currentDate) > 0) {

                            tripArray.add(Trip(driverName, date, eta, route, price, details, passengers, driverEmail, passengerList))
                            uIdArray.add(uId)
                        }

                        adapter.notifyDataSetChanged()
                    }
                }
            }

        upcomingTripBackButton.setOnClickListener {
            val intent = Intent(this, DriverInterface::class.java)
            startActivity(intent)
        }
    }
}
