package com.example.uniride.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp: AppCompatActivity(){

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_sign_up)

         login.setOnClickListener {
             val intent = Intent(this, PassengerDriverSelector::class.java)
             startActivity(intent)
         }
        }

}