package com.example.uniride.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_earnings.*
import kotlinx.android.synthetic.main.activity_earnings.view.*

class Earnings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        db.collection("users").whereEqualTo(
            "email",
            FirebaseAuth.getInstance().currentUser?.email
        ).addSnapshotListener { value, e ->
            if (e != null) {
                Log.w("Earnings", "Listen failed.", e)
                return@addSnapshotListener
            }
            for (doc in value!!.documentChanges) {
                when (doc.type) {
                    DocumentChange.Type.ADDED -> earnings_value.text =
                        doc.document.getDouble("user credits").toString()
                    DocumentChange.Type.MODIFIED -> earnings_value.text =
                        doc.document.getDouble("user credits").toString()
                }
            }
        }

        earnings_back_button.setOnClickListener {
            finish()
        }
        earnings_addCredit_button.setOnClickListener {
            startActivity( Intent(this, AddingCredits::class.java))
        }
    }
}
