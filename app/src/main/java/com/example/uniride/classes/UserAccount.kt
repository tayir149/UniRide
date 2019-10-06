package com.example.uniride.classes

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class UserAccount (firstName:String?,lastName:String?,address:String?, email:String?, userid:String?){
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val userFirstName = firstName
    private val userLastName = lastName
    private val userAddress = address
    private val userEmail = email
    private val fbUserID = userid

    //urd = uniride dollar
    var userURD : Double? = 0.00

    fun saveUserToDatabase(){
        val user = mapOf("email" to userEmail, "first name" to userFirstName,"last name" to userLastName, "address" to userAddress, "user credits" to userURD, "fbUserID" to fbUserID)

        userEmail?.let {
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

    fun addCredit(creditToAdd:Double?, passenger:UserAccount){
        if(passenger.getCredit()!! >= creditToAdd!!){
            removeCredit(creditToAdd)
            userURD = userURD?.plus(creditToAdd!!)
        }
    }
    fun removeCredit(creditToRemove:Double?){
        val remainingCredit = getCredit()?.minus(creditToRemove!!)
        if(remainingCredit!! >=0){
            userURD = userURD?.minus(creditToRemove!!)
        }
    }
    fun getCredit(): Double? {
        return userURD
    }
//
//    fun main(args:Array<String>){
//        val user1 = UserAccount("John","Doe","Address Line 2", "johnd@autuni.ac.nz")
//        val user2 = UserAccount("Jane", "Doe", "Address Line 3", "janed@autuni.ac.nz")
//        println(getCredit())
//
//        user1.addCredit(5.00, user2)
//        println(getCredit())
//    }
}
