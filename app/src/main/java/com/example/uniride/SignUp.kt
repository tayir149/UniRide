package com.example.uniride

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
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