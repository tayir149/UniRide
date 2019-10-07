package com.example.uniride.activities


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_history.*
import org.jetbrains.anko.startActivity


class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        /* When HISTORY BACK button pressed, will navigate to last page */
        history_back_button.setOnClickListener {
            startActivity<PassengerInterface>()
        }

        val listView = findViewById<ListView>(R.id.history_listview)

        listView.adapter = MyCustomAdapter(this)
    }


    private class MyCustomAdapter(context: Context): BaseAdapter() {

        private val mContext: Context

        private val driverNames = arrayListOf<String>(
            " Jason", " Tim", " Tayier", " Jesselle", " Liam"
        )

        private val historyDate = arrayListOf<String>(
            "08 August, 2019", "07 August, 2019", "06 August, 2019", "05 August, 2019", "04 August, 2019"
        )

        private val pickUpTime = arrayListOf<String>(
            " 10:00", "13:45", "9:00", "7:30", "16:10"
        )

        private val timeOfETA = arrayListOf<String>(
            " 10:30", "14:20", "9:16", "7:48", "16:52"
        )

        init {
            this.mContext = context
        }

        override fun getCount(): Int {
            return driverNames.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(position: Int): Any {
            return "TEST"
        }

        //responsible for rendering out each row
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.activity_historyrow, viewGroup, false)

            val positionTextView = rowMain.findViewById<TextView>(R.id.history_num_of_passengers_view)
            positionTextView.text = " $position"

            val nameTextView = rowMain.findViewById<TextView>(R.id.history_driver_name)
            nameTextView.text = driverNames.get(position)

            val dateTextView = rowMain.findViewById<TextView>(R.id.history_trip_date)
            dateTextView.text = historyDate.get(position)

            val pickUpTimeTextView = rowMain.findViewById<TextView>(R.id.history_pickUpTime_view)
            pickUpTimeTextView.text = pickUpTime.get(position)

            val timeOfETATextView = rowMain.findViewById<TextView>(R.id.history_ETA_view)
            timeOfETATextView.text = timeOfETA.get(position)

            val messageDriverButton = rowMain.findViewById<Button>(R.id.history_message_button)
            messageDriverButton.setOnClickListener{
                val userID = "royalty37"
                val intent = Intent(mContext, Messenger::class.java)
                intent.putExtra("userID",userID)
                mContext.startActivity(intent)
            }

            return rowMain
        }
    }
}

