package com.example.uniride.activities


import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import com.example.uniride.classes.Trip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_history.*
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.Boolean as Boolean1


class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        /* When HISTORY BACK button pressed, will navigate to last page */
        history_back_button.setOnClickListener {
            startActivity<PassengerInterface>()
        }

        val dateFormat = SimpleDateFormat("dd-mm-yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
        val tripArray = ArrayList<Trip>()
        val userArray = ArrayList<String>()

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val listView = findViewById<ListView>(R.id.history_listview)
        val adapter = MyCustomAdapter(this, tripArray, userArray)

        listView.adapter = adapter

        val cal = Calendar.getInstance()
        val nowDate = dateFormat.format(cal.time)
        val nowTime = timeFormat.format(cal.time)

        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        currentUserEmail?.let {
            db.collection("users").document(it)
        }
            ?.get()?.addOnSuccessListener { document ->
                val bookedTrips = document.get("booked_trips") as ArrayList<String>


                db.collection("trips")
                    .addSnapshotListener { value, e ->
                        if (e != null) {
                            Log.w("TripList", "Listen failed.", e)
                            return@addSnapshotListener
                        }

                        for (document in value!!.documentChanges) {
                            if (document.type == DocumentChange.Type.ADDED) {
                                val date = document.document.getString("date")
                                val price = document.document.getDouble("price")
                                val eta = document.document.getString("estimated_arrival_time")
                                val passengersNum =
                                    document.document.getLong("number_of_passengers")?.toInt()
                                val userEmail = document.document.getString("user_email")
                                val driverName = document.document.getString("trip_driver")
                                val tripId = document.document.id
                                val route = document.document.getString("route")
                                val details = document.document.getString("car_detail")
                                val passengerList =
                                    document.document.get("passenger_list") as ArrayList<String>?

                                val dateFormatted = dateFormat.parse(date)
                                val currentDate = dateFormat.parse(nowDate)
                                val timeFromDateBase = timeFormat.parse(eta)
                                val currentTime = timeFormat.parse(nowTime)

                                if (!bookedTrips.contains(tripId)) {
                                    if (currentUserEmail!!.compareTo(userEmail!!) != 0) {
                                        if (dateFormatted.compareTo(currentDate) == 0 && timeFromDateBase < currentTime
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
                                                    passengersNum,
                                                    userEmail,
                                                    passengerList
                                                )
                                            )
                                            userArray.add(tripId)
                                        }
                                    }
                                }
                            }
                            //adapter.notifyDateSetChanged()
                            Log.d("TripList", "test &tripArray")
                        }
                    }
            }
    }


    private class MyCustomAdapter(context: Context, tripArray:ArrayList<Trip>, userArray:ArrayList<String>) : BaseAdapter() {

        private val mContext: Context = context
        private val array = tripArray
        //private val uIdArray = userArray


        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        override fun getCount(): Int {
            return array.size
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getItem(p0: Int): Any {
            return "TEST"
        }

//            private val driverNames = arrayListOf<String>(
//                " Jason", " Tim", " Tayier", " Jesselle", " Liam"
//            )
//
//            private val historyDate = arrayListOf<String>(
//                "08 August, 2019",
//                "07 August, 2019",
//                "06 August, 2019",
//                "05 August, 2019",
//                "04 August, 2019"
//            )
//
//            private val pickUpTime = arrayListOf<String>(
//                " 10:00", "13:45", "9:00", "7:30", "16:10"
//            )
//
//            private val timeOfETA = arrayListOf<String>(
//                " 10:30", "14:20", "9:16", "7:48", "16:52"
//            )
//
//            init {
//                this.mContext = context
//            }



        @RequiresApi(Build.VERSION_CODES.O)
        //responsible for rendering out each row
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)

            val rowMain = layoutInflater.inflate(R.layout.activity_historyrow, p2, false)

            val dateTextView = rowMain.findViewById<TextView>(R.id.history_trip_date)
            dateTextView.text = array[p0].getDate()
            val etaTextView = rowMain.findViewById<TextView>(R.id.history_ETA_view)
            etaTextView.text = array[p0].getArrival()
            val noPassengerTextView = rowMain.findViewById<TextView>(R.id.history_num_of_passengers_view)
            noPassengerTextView.text = array[p0].getPassengerNo().toString()
            val price = rowMain.findViewById<TextView>(R.id.history_price_view)
            price.text = "$" + array[p0].getPrice().toString()

//                val rowMain = layoutInflater.inflate(R.layout.activity_historyrow, viewGroup, false)
//
//                val positionTextView =
//                    rowMain.findViewById<TextView>(R.id.history_num_of_passengers_view)
//                positionTextView.text = " $position"
//
//                val nameTextView = rowMain.findViewById<TextView>(R.id.history_driver_name)
//                nameTextView.text = driverNames.get(position)
//
//                val dateTextView = rowMain.findViewById<TextView>(R.id.history_trip_date)
//                dateTextView.text = historyDate.get(position)
//
//                val pickUpTimeTextView =
//                    rowMain.findViewById<TextView>(R.id.history_pickUpTime_view)
//                pickUpTimeTextView.text = pickUpTime.get(position)
//
//                val timeOfETATextView = rowMain.findViewById<TextView>(R.id.history_ETA_view)
//                timeOfETATextView.text = timeOfETA.get(position)

                val passengers = array[p0].getPassengerList()?.toArray()

                Log.d("PassengerList", passengers.toString())

                val messageDriverButton = rowMain.findViewById<Button>(R.id.history_message_button)
                messageDriverButton.setOnClickListener {

                    val p = PopupMenu(mContext, messageDriverButton)
                    //p.menuInflater.inflate(R.menu.message_passenger_menu, p.menu)
                    if (passengers != null) {
                        var i = 0
                        for (item in passengers) {
                            p.menu.add(i, i, i, item.toString())
                            i++
                        }
                    }
                    p.setOnMenuItemClickListener(object: PopupMenu.OnMenuItemClickListener {
                        override fun onMenuItemClick(item: MenuItem): kotlin.Boolean {
                            Toast.makeText(mContext, item.title, Toast.LENGTH_SHORT).show()
                            return false;
                        }
                    })


                    /*
                    val userID = "royalty37"
                    val intent = Intent(mContext, Messenger::class.java)
                    intent.putExtra("userID", userID)
                    mContext.startActivity(intent)
                    */
                }
//
                return rowMain
        }
    }
}


