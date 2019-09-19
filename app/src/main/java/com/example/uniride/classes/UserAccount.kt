package com.example.uniride.classes

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.math.BigDecimal

class UserAccount (firstName:String?,lastName:String?,address:String?, email:String?){
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val userFirstName = firstName
    private val userLastName = lastName
    private val userAddress = address
    private val userEmail = email

    //urd = uniride dollar
    var userURD : Double? = 0.00

    fun saveUserToDatabase(){
        val user = mapOf("email" to userEmail, "first name" to userFirstName,"last name" to userLastName, "address" to userAddress, "user credits" to userURD)

        FirebaseAuth.getInstance().currentUser?.uid?.let {
            db.collection("users").document(it)
                .set(user)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    // else if successful
                    Log.d("Main","Successfully created user")
                }
                .addOnFailureListener(){
                    Log.d("Main", "Failed to create user: ${it.message}")
    //                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT)
    //                    .show()
                }
        }
    }

    fun addCredit(creditToAdd:Double?){
        userURD = userURD?.plus(creditToAdd!!)
    }
    fun removeCredit(creditToRemove:Double?){
        userURD = userURD?.minus(creditToRemove!!)
    }
    fun getCredit(): Double? {
        return userURD
    }


}