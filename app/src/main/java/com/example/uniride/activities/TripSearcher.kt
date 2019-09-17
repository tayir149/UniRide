package com.example.uniride.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import com.example.uniride.showToast
import kotlinx.android.synthetic.main.activity_create_trip.*
import kotlinx.android.synthetic.main.activity_passenger_interface.*
import kotlinx.android.synthetic.main.activity_tripsearcher.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TripSearcher : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    private var dateFormat = SimpleDateFormat("dd/MM/YYYY", Locale.UK)
    private var timeFormat = SimpleDateFormat("hh:mm a", Locale.UK)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tripsearcher)

        //check which button was pressed to enter tripsearcher activity
        val pIntent = intent
        val prevAct = pIntent.getStringExtra("FROM_ACTIVITY")
        when(prevAct){
            "uni-home" -> text_pageTitle_searcher.text = "University to Home"
            "home-uni" -> text_pageTitle_searcher.text = "Home to University"
        }

        //Calendar for current time
        val now = Calendar.getInstance()

        //get date entered by user
        trip_date_searcher.setOnClickListener(){
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

                val dateOfTrip = trip_date_searcher.text.toString()

                //TODO: IF DATE IS TODAY, CHECK IF TIME IS BEFORE CURRENT TIME!
                when {
                    dateOfTrip.isEmpty() -> showToast("Please enter a date first!")
                    else -> trip_time_searcher.setText(time)
                }
            },
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false)
            timePicker.show()
        }

        radioGroup_searcher.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            println(radio)
        }

        button_searchTrip_searcher.setOnClickListener{
            val dateOfTrip = trip_date_searcher.text.toString()
            val timeOfTrip = trip_time_searcher.text.toString()

            //get chosen filter
            var id: Int = radioGroup_searcher.checkedRadioButtonId

            val intent = Intent(this, TripList::class.java)
            when {
                dateOfTrip.isEmpty() -> showToast("Please enter a date first!")
                timeOfTrip.isEmpty() -> showToast("Please enter a time first!")
                id==-1 -> showToast("No filter chosen!")
                else ->  startActivity(intent)
            }
        }
        button_back_searcher.setOnClickListener{
            finish()
        }
    }
}