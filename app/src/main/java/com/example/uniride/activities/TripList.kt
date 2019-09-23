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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_triplist.*
import kotlin.collections.ArrayList

class TripList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_triplist)

        val listView = findViewById<ListView>(R.id.triplist_listView)
//        val tripArray = getTrip()

        //adaptor telling list what to render
        listView.adapter = MyCustomAdapter(this)

        triplist_back_button.setOnClickListener{
            finish()
        }
    }

//    fun getTrip():ArrayList<Trip>{
//        val tripArray = ArrayList<Trip>()
//
//        //get documents
//        val docRef = db.collection("trips")
//            .addSnapshotListener{ value, e ->
//                if(e!=null){
//                    Log.d("TripList","Listen fail.", e)
//                    return@addSnapshotListener
//                }
//
//                for(doc in value!!){
//                    tripArray.add(Trip(doc.getString("trip_driver"),doc.getString("date"),doc.getString("estimated_arrival_time"),
//                        doc.getString("route"),doc.getDouble("price"),doc.getString("car_details"),(doc.getLong("number_of_passengers")))
//                }
//            }
//
//
//        return tripArray
//    }

    private class MyCustomAdapter(context: Context):BaseAdapter(){
        private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        private val mContext: Context = context

        //testing
//        private val date = arrayListOf("2019-09-20","2019-09-21","2019-09-22")
//        private val route = arrayListOf("15:30","18:0","10:30")
//        private val etaValue = arrayListOf(30,20,30)
//        private val noPassengers = arrayListOf(0,2,1)
//        private val carDetails = arrayListOf("1","2","3")
        private var date = arrayListOf<String>()
        private val route = ArrayList<String>()
        private val etaValue = ArrayList<String>()
        private val noPassengers =ArrayList<Long>()
        private val carDetails = ArrayList<String>()

        override fun getItem(p0: Int): Any {return "trial"}
        override fun getItemId(p0: Int): Long {return p0.toLong()}
        override fun getCount(): Int {return date.size}

        init {
            db.collection("trips")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
//                        Log.d("TripList","${document.id} => ${document.data}")
//                        Log.d("TripList","Date: ${document.getString("date")}")
                        document.getString("date")?.let { date.add(it) }
                        document.getString("route")?.let { route.add(it) }
                        document.getString("estimated_arrival_time")?.let { etaValue.add(it) }
                        document.getLong("number_of_passengers")?.let { noPassengers.add(it) }
                        document.getString("car_details")?.let { carDetails.add(it) }
                    }
                }
                .addOnFailureListener{ exception ->
                    Log.d("TripList", "Error getting documents", exception)
                }
//            date.add("test")
//            route.add("10:00")
//            etaValue.add(30)
//            noPassengers.add(2)
//            route.add("test")

            Log.d("TripList","test $date")
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.activity_triprow, p2, false)

            val dateTextView = rowMain.findViewById<TextView>(R.id.triplist_date)
            dateTextView.text = date[p0]
            val pickupTextView = rowMain.findViewById<TextView>(R.id.triplist_pickup_textValue)
            pickupTextView.text = route[p0]
            val etaTextView = rowMain.findViewById<TextView>(R.id.triplist_eta_textValue)
            etaTextView.text = etaValue[p0].toString()
            val noPassengersTextView = rowMain.findViewById<TextView>(R.id.triplist_noPassengers_textValue)
            noPassengersTextView.text = noPassengers[p0].toString()
            val carDetailsView = rowMain.findViewById<TextView>(R.id.triplist_carDetails_textValue)
            carDetailsView.text = carDetails[p0]

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
