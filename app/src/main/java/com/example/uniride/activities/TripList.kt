package com.example.uniride.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import com.example.uniride.classes.Trip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_triplist.*
import kotlin.collections.ArrayList

class TripList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_triplist)


        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val tripArray = ArrayList<Trip>()
        val uIdArray = ArrayList<String>()

        val listView = findViewById<ListView>(R.id.triplist_listView)
        val adapter = MyCustomAdapter(this, tripArray, uIdArray)

        //adaptor telling list what to render
        listView.adapter = adapter

        db.collection("trips")
            .addSnapshotListener{value, e->
                if (e!=null){
                    Log.w("TripList", "Listen failed.", e)
                    return@addSnapshotListener
                }

                for (document in value!!.documentChanges) {
                    if(document.type == DocumentChange.Type.ADDED){
                        var date = document.document.getString("date")
                        var route = document.document.getString("route")
                        var eta = document.document.getString("estimated_arrival_time")
                        var details = document.document.getString("car_detail")
                        var passengers = document.document.getLong("number_of_passengers")?.toInt()
                        val price = document.document.getDouble("price")
                        val driverEmail = document.document.getString("user_email")
                        val driverName = document.document.getString("trip_driver")
                        val uId = document.document.id

                        tripArray.add(Trip(driverName, date, eta, route, price, details, passengers, driverEmail))
                        uIdArray.add(uId)

                        adapter.notifyDataSetChanged()
                        Log.d("TripList", "test $tripArray")
                    }
                }
            }

        triplist_back_button.setOnClickListener {
            finish()
        }
    }

    private class MyCustomAdapter(context: Context, tripArray:ArrayList<Trip>, uIds: ArrayList<String>):BaseAdapter(){
        private val mContext: Context = context
        private val array = tripArray
        private val uIdArray = uIds

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        override fun getItem(p0: Int): Any {return "trial"}
        override fun getItemId(p0: Int): Long {return p0.toLong()}
        override fun getCount(): Int {return array.size}

        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.activity_triprow, p2, false)

            val dateTextView = rowMain.findViewById<TextView>(R.id.triplist_date)
            dateTextView.text = array[p0].getDate()
            val pickupTextView = rowMain.findViewById<TextView>(R.id.triplist_pickup_textValue)
            pickupTextView.text = array[p0].getRouteOfTrip()
            val etaTextView = rowMain.findViewById<TextView>(R.id.triplist_eta_textValue)
            etaTextView.text = array[p0].getArrival()
            val noPassengersTextView = rowMain.findViewById<TextView>(R.id.triplist_noPassengers_textValue)
            noPassengersTextView.text = array[p0].getPassengerNo().toString()
            val carDetailsView = rowMain.findViewById<TextView>(R.id.triplist_carDetails_textValue)
            carDetailsView.text = array[p0].getCar()

            val bookTrip = rowMain.findViewById<Button>(R.id.triplist_book_button)
            bookTrip.setOnClickListener {
                val passengerEmail = FirebaseAuth.getInstance().currentUser?.email
                db.collection("trips").document(uIdArray[p0]).update("passenger_list", FieldValue.arrayUnion(passengerEmail))
                passengerEmail?.let { it1 -> db.collection("users").document(it1).update("booked_trips", FieldValue.arrayUnion(uIdArray[p0])) }
            }


            val messageDriverView = rowMain.findViewById<Button>(R.id.triplist_message_button)
            messageDriverView.setOnClickListener{
                val userID = "royalty37"
                val intent = Intent(mContext, Messenger::class.java)
                intent.putExtra("userID",userID)
                mContext.startActivity(intent)
            }

            return rowMain
        }
    }
}
