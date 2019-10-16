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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SaveTrips : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_trips)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        //val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        val tripArray = ArrayList<Trip>()
        val uIdArray = ArrayList<String>()

        //val listView = findViewById<ListView>(R.id.save_trip_listview)
        //val adapter = MyCustomAdapter(this, tripArray, uIdArray)

        //listView.adapter = adapter

        /*val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        val adapter = saveTripsAdapter(this, tripArray, uIdArray)
        recyclerView.adapter = adapter

        val userProfileRef = currentUserEmail?.let {
            it1 -> db.collection("users").document(it1)
        }

        lateinit var passengerAddress: String
        userProfileRef?.get()?.addOnSuccessListener {
            document ->
            if (document != null) {
                passengerAddress = document.getString("address").toString()

                db.collection("trips").whereArrayContains("passenger_list", passengerAddress)
                    .addSnapshotListener { value, e ->
                        if ( e != null) {
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
                            val passengerlist = document.document.get("passenger_list") as ArrayList<String>?


                            val uId = document.document.id

                            tripArray.add(Trip(driverName, date, eta, route, price, details, passengers, driverEmail, passengerlist))
                            uIdArray.add(uId)
                            adapter.notifyDataSetChanged()
                        }
                    }
            } else {
                showToast("No such document")
            }
        }?.addOnFailureListener {
            exception ->
            Log.d("Main", "get failed with", exception)
        }

        saveTrips_back_button.setOnClickListener {
            finish()
        }

    }


    class saveTripsAdapter(val context: Context, val trips: ArrayList<Trip>, val uIds: ArrayList<String>) : RecyclerView.Adapter<MyViewHolder>() {

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        override fun onCreateViewHolder(parent: ViewGroup, ViewType: Int): MyViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.activity_save_tripsrow, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int{
            return trips.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val trip = trips[position]
            val uId = uIds[position]
            holder.setData(trip, position, uId)
        }

        inner class MyViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {

            private var currentTrip: Trip? = null
            private var currentPosition: Int = 0
            private var currentUId: String? = null

            fun setData(trip: Trip?, position: Int, uId: String) {
                val availableSpace = trip?.getPassengerNo()?.minus(trip.getPassengerList()!!.size)

                itemView.save_trip_date.text = trip!!.getDate()


                this.currentTrip = trip
                this.currentPosition = position
                this.currentUId = uId
            }
        }

    }*/
        val dateFormat = SimpleDateFormat("dd-mm-yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm", Locale.getDefault())

        val listView = findViewById<ListView>(R.id.save_trip_listview)
        val adapter = MyCustomAdapter(this, tripArray)

        listView.adapter = adapter

        val cal = Calendar.getInstance()
        val nowDate = dateFormat.format(cal.time)
        val nowTime = timeFormat.format(cal.time)


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

                                val dateFormatted = dateFormat.parse(date)
                                val currentDate = dateFormat.parse(nowDate)
                                val timeFromDataBase = timeFormat.parse(eta)
                                val currentTime = timeFormat.parse(nowTime)

                                if (!savedTrips.contains((uId))) {
                                    if ((dateFormatted.compareTo(currentDate) == 0 && timeFromDataBase <= currentTime)
                                        || dateFormatted.compareTo(currentDate) < 0
                                    ) {
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



            val dateTextView = rowMain.findViewById<TextView>(R.id.save_trip_date)
            dateTextView.text = array[p0].getDate()



            return rowMain
        }
    }
}
