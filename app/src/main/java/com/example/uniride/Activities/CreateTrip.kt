package com.example.uniride.Activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_create_trip.*
import java.text.SimpleDateFormat
import java.util.*

class CreateTrip : AppCompatActivity() {

    private var dateFormat = SimpleDateFormat("dd/MM/YYYY", Locale.UK)
    private var timeFormat = SimpleDateFormat("hh:mm a", Locale.UK)
    val homeAddress : String = "New Lynn, Auckland"
    val uniAddress : String = "Auckland University of Technology"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)

        //When back button pressed, page will go back to driver interface
        backButton.setOnClickListener{
            val intent = Intent(this, DriverInterface::class.java)
            startActivity(intent)
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


    }
}
