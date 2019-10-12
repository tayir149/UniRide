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
import com.example.uniride.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_triplist.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TripList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_triplist)

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val tripArray = ArrayList<Trip>()
        val uIdArray = ArrayList<String>()

        val listView = findViewById<ListView>(R.id.triplist_listView)
        val adapter = MyCustomAdapter(this, tripArray, uIdArray)

        //adaptor telling list what to render
        listView.adapter = adapter

        //Only gets current user created trips and only gets future trips
        val cal = Calendar.getInstance()
        val nowDate = dateFormat.format(cal.time)
        val nowTime = timeFormat.format(cal.time)

        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        currentUserEmail?.let {
            db.collection("users").document(it)}
            ?.get()?.addOnSuccessListener { document ->
                val bookedTrips = document.get("booked_trips") as ArrayList<String>

        db.collection("trips")
            .addSnapshotListener{value, e->
                if (e!=null){
                    Log.w("TripList", "Listen failed.", e)
                    return@addSnapshotListener
                }

                for (document in value!!.documentChanges) {
                    if(document.type == DocumentChange.Type.ADDED){
                        val date = document.document.getString("date")
                        val route = document.document.getString("route")
                        val eta = document.document.getString("estimated_arrival_time")
                        val details = document.document.getString("car_detail")
                        val passengers = document.document.getLong("number_of_passengers")?.toInt()
                        val price = document.document.getDouble("price")
                        val driverEmail = document.document.getString("user_email")
                        val driverName = document.document.getString("trip_driver")
                        val uId = document.document.id
                        val passengerList = document.document.get("passenger_list") as ArrayList<String>?


                        //In order to correctly compare time and date, it needs to be Date Object
                        val dateFormatted = dateFormat.parse(date)
                        val currentDate = dateFormat.parse(nowDate)
                        val timeFromDataBase = timeFormat.parse(eta)
                        val currentTime = timeFormat.parse(nowTime)

                        //To determine if current user already booked this trip
                        if(!bookedTrips.contains(uId)){
                            //Does not show the trips created by current user
                            if(currentUserEmail!!.compareTo(driverEmail!!) != 0){

                                //Filters the trips only show future trips
                                if((dateFormatted.compareTo(currentDate) == 0 && timeFromDataBase >= currentTime) || dateFormatted.compareTo(currentDate) > 0){

                                    tripArray.add(Trip(driverName, date, eta, route, price, details, passengers, driverEmail, passengerList))
                                    uIdArray.add(uId)
                                }
                            }
                        }
                    }

                    adapter.notifyDataSetChanged()
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

            //gets available seats
            val availableSpace = array[p0].getPassengerNo()?.minus(array[p0].getPassengerList()!!.size)

            val dateTextView = rowMain.findViewById<TextView>(R.id.triplist_date)
            dateTextView.text = array[p0].getDate()
            val pickupTextView = rowMain.findViewById<TextView>(R.id.triplist_pickup_textValue)
            pickupTextView.text = array[p0].getRouteOfTrip()
            val etaTextView = rowMain.findViewById<TextView>(R.id.triplist_eta_textValue)
            etaTextView.text = array[p0].getArrival()
            val noPassengersTextView = rowMain.findViewById<TextView>(R.id.triplist_noPassengers_textValue)
            noPassengersTextView.text = availableSpace.toString()
            val carDetailsView = rowMain.findViewById<TextView>(R.id.triplist_carDetails_textValue)
            carDetailsView.text = array[p0].getCar()
            val price = rowMain.findViewById<TextView>(R.id.triplist_price_textValue)
            price.text = "$" + array[p0].getPrice().toString()
            val bookTrip = rowMain.findViewById<Button>(R.id.triplist_book_button)


            bookTrip.setOnClickListener {

                if(availableSpace!! > 0){
                    val passengerEmail = FirebaseAuth.getInstance().currentUser?.email
                    val userProfileRef = passengerEmail?.let { it1 ->
                        db.collection("users").document(it1) }
                    lateinit var passengerAddress: String
                    userProfileRef?.get()?.addOnSuccessListener { document ->
                        if(document != null){
                            passengerAddress = document.getString("address").toString()
                            db.collection("trips").document(uIdArray[p0]).update("passenger_list", FieldValue.arrayUnion(passengerAddress))
                        } else{
                            Log.d("Error", "No such document")
                        }
                    }
                    passengerEmail?.let { it1 -> db.collection("users").document(it1).update("booked_trips", FieldValue.arrayUnion(uIdArray[p0])) }
                    mContext.showToast("Trip Booked Successfully!")
                    val intent = Intent(mContext, PassengerInterface::class.java)
                    mContext.startActivity(intent)
                }
                else{
                    mContext.showToast("No available seats in this trip!")
                }
            }

            val messageDriverView = rowMain.findViewById<Button>(R.id.triplist_message_button)
            messageDriverView.setOnClickListener{
                val userID = "2659411804110269"
                val intent = Intent(mContext, Messenger::class.java)
                intent.putExtra("userID",userID)
                mContext.startActivity(intent)
            }

            return rowMain
        }
    }
}
