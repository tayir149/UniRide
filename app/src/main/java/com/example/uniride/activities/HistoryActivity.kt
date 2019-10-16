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
        val uIdArray = ArrayList<String>()

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val listView = findViewById<ListView>(R.id.history_listview)
        val adapter = MyCustomAdapter(this, tripArray, uIdArray)

        listView.adapter = adapter

        val cal = Calendar.getInstance()
        val nowDate = dateFormat.format(cal.time)
        val nowTime = timeFormat.format(cal.time)

        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        currentUserEmail?.let {
            db.collection("users").document(it)
        }
            ?.get()?.addOnSuccessListener { document ->
                val historyTrips = document.get("booked_trips") as ArrayList<String>


                db.collection("trips")
                    .addSnapshotListener { value, e ->
                        if (e != null) {
                            Log.w("HistoryActivity", "Listen failed.", e)
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

                                if (!historyTrips.contains(tripId)) {
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
                                        uIdArray.add(tripId)
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }
            }
    }


    private class MyCustomAdapter(context: Context , tripArray:ArrayList<Trip>, uIds: ArrayList<String>) : BaseAdapter() {

        private val mContext: Context = context
        private val array = tripArray
        private val uIdArray = uIds

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


        @RequiresApi(Build.VERSION_CODES.O)
        //responsible for rendering out each row
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)

            val rowMain = layoutInflater.inflate(R.layout.activity_historyrow, p2, false)


            val nameTextView = rowMain.findViewById<TextView>(R.id.history_driver_name)
            nameTextView.text = array[p0].getTripDriver()
            val dateTextView = rowMain.findViewById<TextView>(R.id.history_trip_date)
            dateTextView.text = array[p0].getDate()
            val etaTextView = rowMain.findViewById<TextView>(R.id.history_ETA_view)
            etaTextView.text = array[p0].getArrival()
            val noPassengerTextView = rowMain.findViewById<TextView>(R.id.history_num_of_passengers_view)
            noPassengerTextView.text = array[p0].getPassengerNo().toString()
            val price = rowMain.findViewById<TextView>(R.id.history_price_view)
            price.text = "$" + array[p0].getPrice().toString()


                //val passengers = array[p0].getPassengerList()?.toArray()

                //Log.d("PassengerList", passengers.toString())

                /*
                val messageDriverButton = rowMain.findViewById<Button>(R.id.history_message_button)
                messageDriverButton.setOnClickListener {

                    val p = PopupMenu(mContext, messageDriverButton)
                    //p.menuInflater.inflate(R.menu.message_passenger_menu, p.menu)
                    if (passengers != null) {
                        var i = 0
                        for (item in passengers) {
                            val docRef = db.collection("users").whereEqualTo("address", item)
                                .get().addOnSuccessListener { document ->
                                    if (document != null)
                                        p.menu.add(i, i, i, document.toString())
                                }
                            i++
                        }
                    }
                    p.setOnMenuItemClickListener(object: PopupMenu.OnMenuItemClickListener {
                        override fun onMenuItemClick(item: MenuItem): kotlin.Boolean {
                            Toast.makeText(mContext, item.title, Toast.LENGTH_SHORT).show()
                            return false;
                        }
                    })
                    p.show()

                    /*
                    val userID = "royalty37"
                    val intent = Intent(mContext, Messenger::class.java)
                    intent.putExtra("userID", userID)
                    mContext.startActivity(intent)
                    */
                }*/

            val messengerDriverButton = rowMain.findViewById<Button>(R.id.history_message_button)
            messengerDriverButton.setOnClickListener {
                /*val userID = "royalty37"
                val intent = Intent(mContext, Messenger::class.java)
                intent.putExtra("userID", userID)
                mContext.startActivity(intent)*/
                val driverEmail = array[p0].getDriverEmail()
                Log.d("Email", driverEmail)

                lateinit var fbUserID: String

                val docRef = db.collection("users").document(driverEmail)
                Log.d("docRef", docRef.toString())

                docRef.get().addOnSuccessListener {
                    document ->
                    if (document != null) {
                        fbUserID = document.getString("fbUserID").toString()
                        Log.d("Success", "Retrived doc")
                        val intent = Intent(mContext, Messenger::class.java)
                        intent.putExtra("userID", fbUserID)
                        mContext.startActivity(intent)
                    } else {
                        Log.d("Fail", "Document didn't retrieve")
                    }
                }
            }

            return rowMain
        }
    }
}


