package com.example.uniride.activities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_triprow.*
import org.jetbrains.anko.startActivity

class TripList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_triplist)

        val listView = findViewById<ListView>(R.id.triplist_listView)

        //adaptor telling list what to render
        listView.adapter = MyCustomAdapter(this)

//        triplist_message_button.setOnClickListener{
//            val userID = "royalty37"
//            startActivity<Messenger>("userID" to userID)
//        }
    }
    private class MyCustomAdapter(context: Context):BaseAdapter(){
        private val mContext: Context
        private val names = arrayListOf<String>(
            "Jekk", "Bruv", "Yeet"
        )

        init {
            mContext = context
        }

        override fun getItem(p0: Int): Any {return "womp womp"}
        override fun getItemId(p0: Int): Long {return p0.toLong()}
        override fun getCount(): Int {return names.size}

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.activity_triprow, p2, false)

            val nameTextView = rowMain.findViewById<TextView>(R.id.triplist_driver_text)
            nameTextView.text = "Driver:"+names.get(p0)

            val positionTextView = rowMain.findViewById<TextView>(R.id.triplist_pickup_text)
            positionTextView.text = "Pickup Time: $p0"

            return rowMain
        }
    }
}
