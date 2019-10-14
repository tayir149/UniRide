package com.example.uniride.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import com.example.uniride.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_addingcredit.*
import kotlinx.android.synthetic.main.activity_earnings.*
import kotlinx.android.synthetic.main.activity_earnings.view.*

class AddingCredits : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addingcredit)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        addingCredit_confirm_button.setOnClickListener{
            val userInput = addingCredit_value.text.toString()

            if(userInput.isNotEmpty()){
                val creditToAdd = userInput.toDouble()
                if(creditToAdd>0){
                    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
                    val userProfileRef = currentUserEmail?.let {it ->
                        db.collection("users").document(it)
                    }

                    userProfileRef?.get()?.addOnSuccessListener { document ->
                        if(document!=null){
                            var currentUserCredits = document.getDouble("user credits")
                            currentUserCredits = currentUserCredits?.plus(creditToAdd)

                            val batch = db.batch()
                            batch.update(userProfileRef,"user credits", currentUserCredits)
                            batch.commit()

                            showToast("Credits added!")
                            finish()
                        }
                    }
                }else showToast("Enter a positive value!")
            }
            else showToast("Please enter a value!")
        }
        addingCredit_cancel_button.setOnClickListener{
            finish()
        }
    }
}
