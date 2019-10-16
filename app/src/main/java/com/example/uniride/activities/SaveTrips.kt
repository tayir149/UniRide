package com.example.uniride.activities

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniride.R
//import com.example.uniride.activities.SaveTrips.saveTripsAdapter.*
import com.example.uniride.classes.Trip
import com.example.uniride.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_save_trips.*
import kotlinx.android.synthetic.main.activity_save_tripsrow.*
import kotlinx.android.synthetic.main.activity_save_tripsrow.view.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SaveTrips : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_trips)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val tripArray = ArrayList<Trip>()
        val uIdArray = ArrayList<String>()



        val listView = findViewById<ListView>(R.id.save_trip_listview)
        val adapter = MyCustomAdapter(this, tripArray)

        listView.adapter = adapter



        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        currentUserEmail?.let {
            db.collection("users").document(it)
        }
            ?.get()?.addOnSuccessListener {
                document ->
                val savedTrips = document.get("booked_trips") as ArrayList<String>


                db.collection("trips")
                    .addSnapshotListener {
                        value, e ->
                        if (e != null) {
                            Log.w("SaveTrips", "Listen failed.", e)
                            return@addSnapshotListener
                        }

                        for (document in value!!.documentChanges) {
                            if (document.type == DocumentChange.Type.ADDED) {
                                val date = document.document.getString("date")
                                val route = document.document.getString("route")
                                val eta = document.document.getString("estimated_arrival_time")
                                val details = document.document.getString("car_detail")
                                val passengers =
                                    document.document.getLong("number_of_passengers")?.toInt()
                                val price = document.document.getDouble("price")
                                val driverEmail = document.document.getString("user_email")
                                val driverName = document.document.getString("trip_driver")
                                val uId = document.document.id
                                val passengerList =
                                    document.document.get("passenger_list") as ArrayList<String>?


                                if (!savedTrips.contains((uId))) {

                                        tripArray.add(
                                            Trip(
                                                driverName,
                                                date,
                                                eta,
                                                route,
                                                price,
                                                details,
                                                passengers,
                                                driverEmail,
                                                passengerList
                                            )
                                        )
                                        uIdArray.add(uId)
                                    }

                            }

                            adapter.notifyDataSetChanged()
                        }
                    }
            }

        saveTrips_back_button.setOnClickListener {
            finish()
        }

    }

    private class MyCustomAdapter(context: Context, tripArray:ArrayList<Trip>):BaseAdapter() {
        private val mContext: Context = context
        private val array = tripArray




        override fun getItem(p0: Int): Any {return "test"}
        override fun getItemId(p0: Int): Long {return p0.toLong()}
        override fun getCount(): Int {return array.size}


        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.activity_save_tripsrow, p2, false)



            val availableSeats = array[p0].getPassengerNo()?.minus(array[p0].getPassengerList()!!.size)
            val dateTextView = rowMain.findViewById<TextView>(R.id.save_trip_date)
            dateTextView.text = " " + array[p0].getDate()
            val routeTextView = rowMain.findViewById<TextView>(R.id.save_trip_route_view)
            routeTextView.text = " " + array[p0].getRouteOfTrip()
            val etaTextView = rowMain.findViewById<TextView>(R.id.save_trip_ETA_view)
            etaTextView.text = " " + array[p0].getArrival()
            val availableSeatTextView = rowMain.findViewById<TextView>(R.id.save_trip_seat_view)
            availableSeatTextView.text = " " + availableSeats.toString()
            val carDetailsTextView = rowMain.findViewById<TextView>(R.id.save_trip_car_view)
            carDetailsTextView.text = " " + array[p0].getCar()
            val price = rowMain.findViewById<TextView>(R.id.save_trip_price_view)
            price.text = " " + "$" + array[p0].getPrice().toString()



            return rowMain
        }
    }
}
