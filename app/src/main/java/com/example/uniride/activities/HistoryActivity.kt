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

        val listView = findViewById<ListView>(R.id.history_listview)

        //listView.adapter = MyCustomAdapter(this)

        /* When HISTORY BACK button pressed, will navigate to last page */
        history_back_button.setOnClickListener {
            finish()
        }



    }
}

