package com.example.uniride.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.uniride.R
import com.example.uniride.classes.UserAccount
import com.example.uniride.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_trip.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*

class CreateTrip : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)

        val db = FirebaseFirestore.getInstance()
        val uIdRef = FirebaseAuth.getInstance().currentUser?.uid

        lateinit var  driverName: String
        var dateFormat = SimpleDateFormat("dd/MM/YYYY", Locale.UK)
        var timeFormat = SimpleDateFormat("hh:mm a", Locale.UK)
        lateinit var  homeAddress : String
        val uniAddress : String = "Auckland University of Technology"
        var route : String = "Unknown Route"

        val docRef = uIdRef?.let { db.collection("users").document(it) }
        docRef?.get()?.addOnSuccessListener { document ->
            if (document != null) {
                homeAddress = document.getString("address").toString()
                var firstName = document.getString("first name")
                var lastName = document.getString("last name")
                driverName = "$firstName $lastName"

            } else {
                Log.d("Main", "No such document")
            }
        }?.addOnFailureListener { exception ->
            Log.d("Main", "get failed with ", exception)
        }

        //When back button pressed, page will go back to driver interface
        backButton.setOnClickListener{
            startActivity<DriverInterface>()
        }

        //Calendar for getting current date and time
        val now = Calendar.getInstance()

        date_of_trip_in_create.setOnClickListener {

            //Shows the date picker and takes the user selected date and shows on date text view
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, mYear)
                selectedDate.set(Calendar.MONTH, mMonth)
                selectedDate.set(Calendar.DAY_OF_MONTH, mDay)
                val date = dateFormat.format(selectedDate.time)

                //if selected date is earlier than current date, shows message:Please enter future date!
                if(selectedDate.time < now.time){
                    val toast = Toast.makeText(this, "Please enter future date!", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.TOP, 10, 700)
                    toast.show()
                }
                else date_of_trip_in_create.setText(date)
            },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        //Shows time picker and takes the user selected time and shows on time text view
        arrivalTime.setOnClickListener{
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                val time = timeFormat.format(selectedTime.time)
                arrivalTime.setText(time)
            },
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false)
            timePicker.show()
        }

        homeToUni.setOnClickListener {
            route  = "From $homeAddress to $uniAddress"
        }

        uniToHome.setOnClickListener{
            route = "From $uniAddress to $homeAddress"
        }

        //When Create trip button pressed, information entered will be sent to confirmation page
        createTripButton.setOnClickListener {

            val dateOfTrip = date_of_trip_in_create.text.toString()
            val eta = arrivalTime.text.toString()
            val priceOfTrip = java.lang.Double.parseDouble(enterPrice.text.toString())
            val carDetail = enterCarMake.text.toString()
            val numberOfPassenger = Integer.parseInt(numberOfPassenger.text.toString())

            //If any of the field empty, will not proceed until every field filled
            when {
                dateOfTrip.isEmpty() -> showToast("Please enter a date for the trip")
                eta.isEmpty() -> showToast("Please enter an arrival time for the trip")
                route == "Unknown Route" -> showToast("Please select the route")
                priceOfTrip == 0.00 -> showToast("Please enter a price for the trip")
                carDetail.isEmpty() -> showToast("Please enter you car make and model")
                numberOfPassenger == 0 -> showToast("Please enter number of passenger")
                else -> {
                    //Sends the information to Create trip confirmation page
                    startActivity<CreateTripConfirmationActivity>("Date of trip" to dateOfTrip,
                        "Arrival time" to eta,
                        "Price of trip" to priceOfTrip,
                        "Car details" to carDetail,
                        "Route of trip" to route,
                        "Number of passenger" to numberOfPassenger,
                        "Driver Name" to driverName)
                }
            }
        }
    }
}
