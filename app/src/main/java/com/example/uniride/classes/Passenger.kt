package com.example.uniride.classes

import com.google.firebase.firestore.FirebaseFirestore

class Passenger(email: String? = null) {

    private val passengerEmail: String? = email
    private var passengerName: String? = null
    private var passengerAddress: String? = null

    private val db = FirebaseFirestore.getInstance()

    init{
        if(passengerEmail != null){
            db.collection("users").document(passengerEmail).get().addOnSuccessListener { document ->

                val firstName = document.getString("first name")
                val lastName = document.getString("last name")
                passengerName = "$firstName $lastName"

                passengerAddress = document.getString("address")
            }
        }
    }



}