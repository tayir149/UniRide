package com.example.uniride.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uniride.R
import com.example.uniride.classes.Trip
import com.example.uniride.classes.TripsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_upcoming.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UpcomingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        var dateFormat = SimpleDateFormat("dd-MM-YYYY", Locale.UK)
        var timeFormat = SimpleDateFormat("HH:mm", Locale.UK)
        val tripArray = ArrayList<Trip>()
        val uIdArray = ArrayList<String>()

        //Define a layout manager for recycler view
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        //Link the recycler view with the costume adapter and pass in the Trip array as data
        val adapter = TripsAdapter(this, tripArray, uIdArray)
        recyclerView.adapter = adapter


        //Only gets current user created trips and only gets future trips
        val cal = Calendar.getInstance()
        val nowDate = dateFormat.format(cal.time)
        val nowTime = timeFormat.format(cal.time)

        //Inorder to correctly compare time, it needs to be Date Object
        val currentTime = timeFormat.parse(nowTime)


        db.collection("trips").whereEqualTo("user_email",
            FirebaseAuth.getInstance().currentUser?.email
        ).addSnapshotListener{value, e->
                if (e!=null){
                    Log.w("TripList", "Listen failed.", e)
                    return@addSnapshotListener
                }

                for (document in value!!.documentChanges) {
                    if(document.type == DocumentChange.Type.ADDED){
                        val driverName = document.document.getString("trip_driver")
                        var date = document.document.getString("date")
                        var route = document.document.getString("route")
                        var eta = document.document.getString("estimated_arrival_time")
                        var details = document.document.getString("car_detail")
                        var passengers = document.document.getLong("number_of_passengers")?.toInt()
                        var price = document.document.getDouble("price")
                        val driverEmail = document.document.getString("user_email")

                        //Inorder to correctly compare time, it needs to be Date Object
                        val timeFromDataBase = timeFormat.parse(eta)

                        //Saves the document Uid reference for editing and deleting trips
                        val uId = document.document.id

                        //Filters the trips only show future trips
                        if (date!!.compareTo(nowDate) == 0 && timeFromDataBase >= currentTime) {

                            tripArray.add(Trip(driverName, date, eta, route, price, details, passengers, driverEmail))
                            uIdArray.add(uId)
                        }
                        else if (date.compareTo(nowDate) > 0) {

                            tripArray.add(Trip(driverName, date, eta, route, price, details, passengers, driverEmail))
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
