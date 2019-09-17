package com.example.uniride.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUp: AppCompatActivity(){
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_sign_up)

         login.setOnClickListener {
             val email = username.text.toString()
             val password = password.text.toString()

             if (email.isEmpty() || password.isEmpty()){
                 Toast.makeText(this, "Please enter text in the email/password", Toast.LENGTH_SHORT).show()
                 return@setOnClickListener
             }

             val intent = Intent(this, PassengerDriverSelector::class.java)
             startActivity(intent)

             Log.d("SignUp.kt", "Email is: " +email)
             Log.d("SignUp.kt", "Password: $password")

             FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                 .addOnCompleteListener {
                     if (!it.isSuccessful) return@addOnCompleteListener

                     // else if successful
                     //Log.d("Main","Successfully created user with uid: ${it.result.user.uid}")
                 }
         }

         loginherebutton.setOnClickListener {
             Log.d("SignUp.kt", "Try to show login activity")
             val intent = Intent(this, LogIn::class.java)
             startActivity(intent)
         }
     }
}