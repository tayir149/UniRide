package com.example.uniride.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.login

class LogIn: AppCompatActivity(){

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_login)

         login.setOnClickListener {
             val intent = Intent(this, PassengerDriverSelector::class.java)
             startActivity(intent)
         }

         signupnowbutton.setOnClickListener {
             val intent = Intent(this, SignUp::class.java)
             startActivity(intent)
         }
     }

}