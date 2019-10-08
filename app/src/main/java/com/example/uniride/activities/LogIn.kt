package com.example.uniride.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import com.example.uniride.showToast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.login

class LogIn: AppCompatActivity() {

    var mAuth = FirebaseAuth.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login.setOnClickListener {view ->
            val email = university_email.text.toString();
            val password = password_login.text.toString();

            val intent = Intent(this, PassengerDriverSelector::class.java)
            when {
                email.isEmpty() -> showToast("Please enter your email!")
                password.isEmpty() -> showToast("Please enter your password!")
                else ->{
                    signIn(email, password)
                }
            }
        }

        signupnowbutton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }
    fun signIn(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult>{task ->
            if(task.isSuccessful){
                var intent = Intent(this, PassengerDriverSelector::class.java)
                intent.putExtra("id", mAuth.currentUser?.email)
                startActivity(intent)
            }
            else{
                showToast("Error: ${task.exception?.message}")
            }
        })
    }
}
