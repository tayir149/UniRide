package com.example.uniride.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.login

class LogIn: AppCompatActivity(){

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_login)

         login.setOnClickListener {
             val email = university_email.text.toString()
             val password = password_login.text.toString()

             FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
             //if (!it.

             // else if successful
             //Log.d("Main","Successfully created user with uid: ${it.result.user.uid}")
             //.addFailureListener


             //val intent = Intent(this, PassengerDriverSelector::class.java)
             //startActivity(intent)
         }

         signupnowbutton.setOnClickListener {
             val intent = Intent(this, SignUp::class.java)
             startActivity(intent)
         }
     }

}