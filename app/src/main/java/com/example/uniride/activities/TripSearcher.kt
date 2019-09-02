package com.example.uniride.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import java.util.*

class TripSearcher : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tripsearcher)
    }
    
    //Calendar for getting current date and time
    val now = Calendar.getInstance()
}