package com.example.uniride.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_driver_history.*
import org.jetbrains.anko.startActivity

class DriverHistory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_history)

        //when driver history button pressed, will switch to driver interface
        driver_history_back_button.setOnClickListener {
            startActivity<DriverInterface>()
        }
    }
}
