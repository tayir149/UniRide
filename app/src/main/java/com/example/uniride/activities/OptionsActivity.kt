package com.example.uniride.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_option.*

class OptionsActivity : AppCompatActivity(){
    var fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)

        logOutButton.setOnClickListener {
            val intent = Intent(this, LogIn :: class.java)
            startActivity(intent)
            signOut()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    fun signOut(){
        fbAuth.signOut()
    }
}