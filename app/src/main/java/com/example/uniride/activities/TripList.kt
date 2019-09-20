package com.example.uniride.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_triplist.*
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class TripList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_triplist)

        val listView = findViewById<ListView>(R.id.triplist_listView)

        //adaptor telling list what to render
        listView.adapter = MyCustomAdapter(this)

        triplist_back_button.setOnClickListener{
            finish()
        }
    }

    private class MyCustomAdapter(context: Context):BaseAdapter(){
        private val mContext: Context = context

        //testing
        private val date = arrayListOf("2019-09-20","2019-09-21","2019-09-22")
        private val pickupTime = arrayListOf("15:30","18:0","10:30")
        private val etaValue = arrayListOf(30,20,30)
        private val noPassengers = arrayListOf(0,2,1)

        override fun getItem(p0: Int): Any {return "trial"}
        override fun getItemId(p0: Int): Long {return p0.toLong()}
        override fun getCount(): Int {return date.size}

        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.activity_triprow, p2, false)

            val dateTextView = rowMain.findViewById<TextView>(R.id.triplist_date)
            dateTextView.text = date[p0]
            val pickupTextView = rowMain.findViewById<TextView>(R.id.triplist_pickup_textValue)
            pickupTextView.text = pickupTime[p0].toString()
            val etaTextView = rowMain.findViewById<TextView>(R.id.triplist_eta_textValue)
            etaTextView.text = "${etaValue[p0]} minutes"
            val noPassengersTextView = rowMain.findViewById<TextView>(R.id.triplist_noPassengers_textValue)
            noPassengersTextView.text = noPassengers[p0].toString()

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
