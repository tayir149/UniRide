package com.example.uniride.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.uniride.R
import com.example.uniride.classes.Trip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_driver_history.*
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*

class DriverHistory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_history)

        //when driver history button pressed, will switch to driver interface
        driver_history_back_button.setOnClickListener {
            startActivity<DriverInterface>()
        }


        val dateFormat = SimpleDateFormat("dd-mm-yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
        val tripArray = ArrayList<Trip>()
        val userArray = ArrayList<String>()

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val listView = findViewById<ListView>(R.id.driver_history_listview)
        val adapter = MyCustomAdapter(this, tripArray)


        listView.adapter = adapter

        val cal = Calendar.getInstance()
        val nowDate = dateFormat.format(cal.time)
        val nowTime = timeFormat.format(cal.time)

        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        currentUserEmail?.let {
            db.collection("users").document(it)
        }
            ?.get()?.addOnSuccessListener { document ->
                val driverHistoryTrips = document.get("created_trips") as ArrayList<String>

                db.collection("trips")
                    .addSnapshotListener { value, e ->
                        if (e != null) {
                            Log.w("DriverHistory", "Listen failed.", e)
                            return@addSnapshotListener
                        }

                        for (document in value!!.documentChanges) {
                            if (document.type == DocumentChange.Type.ADDED) {
                                val date = document.document.getString("date")
                                val price = document.document.getDouble("price")
                                val eta = document.document.getString("estimated_arrival_time")
                                val passengersNum = document.document.getLong("number_of_passengers")?.toInt()
                                val userEmail = document.document.getString("user_email")
                                val driverName = document.document.getString("trip_driver")
                                val tripID = document.document.id
                                val route = document.document.getString("route")
                                val details = document.document.getString("car_detail")
                                val passengerList = document.document.get("passenger_list") as ArrayList<String>?

                                val dateFormatted = dateFormat.parse(date)
                                val currentDate = dateFormat.parse(nowDate)
                                val timeFromDateBase = timeFormat.parse(eta)
                                val currentTime = timeFormat.parse(nowTime)

                                if (!driverHistoryTrips.contains(tripID)) {
                                    if (dateFormatted.compareTo(currentDate) == 0 && timeFromDateBase < currentTime
                                        || dateFormatted.compareTo(currentDate) < 0 ) {

                                        tripArray.add(Trip(driverName, date, eta, route, price, details, passengersNum, userEmail, passengerList))
                                        userArray.add(tripID)
                                    }

                                }
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }
            }
    }


    private class MyCustomAdapter(context: Context, tripArray: ArrayList<Trip>): BaseAdapter() {

        private val mContext: Context = context
        private val array = tripArray



        override fun getItem(p0: Int): Any {
            return "test"
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return array.size
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)



            val rowMain = layoutInflater.inflate(R.layout.activity_driver_historyrow, p2, false)

            val dateTextView = rowMain.findViewById<TextView>(R.id.driver_history_date)
            dateTextView.text = array[p0].getDate()

            val price = rowMain.findViewById<TextView>(R.id.driver_history_price_view)
            price.text = "$" + array[p0].getPrice().toString()

            //val passengerListView = rowMain.findViewById<Spinner>(R.id.driver_history_passenger_list)

            val etaTextView = rowMain.findViewById<TextView>(R.id.driver_history_ETA_view)
            etaTextView.text = array[p0].getArrival()

            val noPassengerTextView = rowMain.findViewById<TextView>(R.id.driver_history_passenger_view)
            noPassengerTextView.text = array[p0].getPassengerNo().toString()

            val messengerPassengerButton = rowMain.findViewById<Button>(R.id.driver_history_message_button)
            messengerPassengerButton.setOnClickListener {
                val userID = "royalty37"
                val intent = Intent(mContext, Messenger::class.java)
                intent.putExtra("userID", userID)
                mContext.startActivity(intent)
            }
            
            return rowMain

        }



    }
}
