package com.example.uniride.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_create_trip.*
import kotlinx.android.synthetic.main.activity_tripsearcher.*
import java.text.SimpleDateFormat
import java.util.*

class TripSearcher : AppCompatActivity() {
    private var dateFormat = SimpleDateFormat("dd/MM/YYYY", Locale.UK)
    private var timeFormat = SimpleDateFormat("hh:mm a", Locale.UK)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tripsearcher)

        //Calendar for current time
        val now = Calendar.getInstance()

        //get date entered by user
        trip_date_searcher.setOnClickListener{
            //Shows the date picker and takes the user selected date and shows on date text view
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, mYear)
                selectedDate.set(Calendar.MONTH, mMonth)
                selectedDate.set(Calendar.DAY_OF_MONTH, mDay)
                val date = dateFormat.format(selectedDate.time)

                //if selected date is earlier than current date
                if(selectedDate.time < now.time){
                    val toast = Toast.makeText(this, "Please enter future date!", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.TOP, 10, 700)
                    toast.show()
                }
                else trip_date_searcher.setText(date)
            },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
                datePicker.show()

        }

        //Shows time picker and takes the user selected time and shows on time text view
        trip_time_searcher.setOnClickListener{
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                val time = timeFormat.format(selectedTime.time)

                //if selected time is earlier than current date
                if(selectedTime.time < now.time){
                    val toast = Toast.makeText(this,"Please enter a later time!", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.TOP, 10, 700)
                    toast.show()
                }
                else trip_time_searcher.setText(time)
            },
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false)
            timePicker.show()
        }
    }
}