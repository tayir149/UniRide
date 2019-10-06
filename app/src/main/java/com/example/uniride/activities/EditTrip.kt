package com.example.uniride.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import com.example.uniride.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_edit_trip.*
import java.text.SimpleDateFormat
import java.util.*

class EditTrip : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_trip)

        var dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        var time = SimpleDateFormat("HH:mm", Locale.getDefault())
        val now = Calendar.getInstance()
        val selectedDate = Calendar.getInstance()

        var route = "Unknown Route"
        lateinit var  homeAddress : String
        val uniAddress = "Auckland University of Technology"

        val db = FirebaseFirestore.getInstance()
        val uIdRef = FirebaseAuth.getInstance().currentUser?.email

        val bundle: Bundle? = intent.extras
        date_of_trip_in_edit.setText(bundle!!.getString("Date"))
        arrivalTime.setText(bundle.getString("Eta"))
        enterPrice.setText(bundle.getDouble("Price").toString())
        enterCarMake.setText(bundle.getString("Car Detail"))
        numberOfPassenger.setText(bundle.getInt("NoPassengers").toString())
        val uId = bundle.getString("uId")

        val docRef = uIdRef?.let { db.collection("users").document(it) }
        docRef?.get()?.addOnSuccessListener { document ->
            if (document != null) {
                homeAddress = document.getString("address").toString()

            } else {
                Log.d("Main", "No such document")
            }
        }?.addOnFailureListener { exception ->
            Log.d("Main", "get failed with ", exception)
        }

        date_of_trip_in_edit.setOnClickListener {

            //Shows the date picker and takes the user selected date and shows on date text view
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->

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
                else date_of_trip_in_edit.setText(date)
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
                val timeSelected = time.format(selectedTime.time)

                //When selected date is current date and selected time is earlier than current time, shows message:Please enter future time!
                if(selectedDate.time == now.time && selectedTime.time <= now.time){
                    val toast = Toast.makeText(this, "Please enter future time!", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.TOP, 10, 700)
                    toast.show()
                }
                else{
                    arrivalTime.setText(timeSelected)
                }
            },
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false)
            timePicker.show()
        }

        homeToUni_radio_button.setOnClickListener {
            route  = "From $homeAddress to $uniAddress"
        }

        uniToHome_radio_button.setOnClickListener{
            route = "From $uniAddress to $homeAddress"
        }

        //When Create trip button pressed, information entered will be sent to confirmation page
        confirmTripButton.setOnClickListener {

            val dateOfTrip = date_of_trip_in_edit.text.toString()
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
                    val batch = db.batch()
                    val tripRef = db.collection("trips").document(uId!!)
                    batch.update(tripRef, "date", dateOfTrip)
                    batch.update(tripRef, "estimated_arrival_time", eta)
                    batch.update(tripRef, "route", route)
                    batch.update(tripRef, "price", priceOfTrip)
                    batch.update(tripRef, "car_detail", carDetail)
                    batch.update(tripRef, "number_of_passengers", numberOfPassenger)
                    batch.commit().addOnCompleteListener{
                        showToast("Trip Updated!")
                    }
                    Log.d("editing", uId)
                    Log.d("editing", dateOfTrip)
                    /*uId?.let { it1 ->
                        db.collection("users").document(it1)
                            .update(
                                "date", dateOfTrip,
                                "estimated_arrival_time", eta,
                                "route", route,
                                "price", priceOfTrip,
                                "car_detail", carDetail,
                                "number_of_passengers", numberOfPassenger
                            )
                    }*/

                    val intent = Intent(this, UpcomingActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        backButton.setOnClickListener {
            finish()
        }

        cancel_button.setOnClickListener {

            finish()
        }





    }
}