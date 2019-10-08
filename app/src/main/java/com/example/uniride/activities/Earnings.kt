package com.example.uniride.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import com.example.uniride.classes.Trip
import com.example.uniride.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_earnings.*
import kotlinx.android.synthetic.main.activity_triplist.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Earnings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

       db.collection("users").whereEqualTo("email",
            FirebaseAuth.getInstance().currentUser?.email).addSnapshotListener{value, e->
            if (e!=null){
                Log.w("Earnings", "Listen failed.", e)
                return@addSnapshotListener
            }
           for(doc in value!!.documentChanges){
               when(doc.type){
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
    }
}
