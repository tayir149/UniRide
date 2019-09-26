package com.example.uniride.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_upcoming.*
import org.jetbrains.anko.startActivity

class UpcomingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming)

        upcomingTripBackButton.setOnClickListener {
            finish()
        }
    }
}
