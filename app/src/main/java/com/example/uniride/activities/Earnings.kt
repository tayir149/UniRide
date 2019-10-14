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
import kotlinx.android.synthetic.main.activity_earnings.*
import kotlinx.android.synthetic.main.activity_earnings.view.*

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
        earnings_addCredit_button.setOnClickListener{
            val creditDialog = AlertDialog.Builder(this)
            creditDialog.setTitle("How much credit do you want to add?")
            val view = layoutInflater.inflate(R.layout.dialog_addingcredit, null)
            creditDialog.setView(view)

            creditDialog.setPositiveButton(android.R.string.ok){ dialog,id->
                val creditToAdd = view.earnings_addCredit_button.text.toString()

                if(creditToAdd.isNotEmpty()){
                    val creditToAddD = creditToAdd.toDouble()

                    if(creditToAddD > 0){
                        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
                        val userProfileRef = currentUserEmail?.let { it ->
                            db.collection("users").document(it)
                        }
                        userProfileRef?.get()?.addOnSuccessListener { document ->
                            if (document != null) {
                                val currentUserCredits = document.getDouble("user credits")!!
                                Log.d("Test","current: $currentUserCredits")
                                val newCredit = currentUserCredits + creditToAddD
                                Log.d("Test","new: $newCredit")

                                val batch = db.batch()
                                batch.update(userProfileRef, "user credits", newCredit)
                                batch.commit()
                            } else {
                                Log.d("Error", "No such document")
                            }
                        }
                    }
                    else showToast("Please enter a value more than 0!")
                }
                finish()
                startActivity(getIntent())
            }
            creditDialog.setNegativeButton(android.R.string.cancel){dialog, id ->
                dialog.dismiss()
            }
            creditDialog.show()
        }
    }
}
