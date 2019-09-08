package com.example.uniride.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_history.*
import org.jetbrains.anko.startActivity

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        //TEST
        button6.setOnClickListener {
            val userID = "royalty37"
            startActivity<Messenger>("userID" to userID)
        }
    }
}
