package com.example.uniride.classes

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uniride.R
import com.example.uniride.activities.BookedTrips
import com.example.uniride.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.booked_trip_list.view.*

class BookedTripAdapter(val context: Context, val  trips: ArrayList<Trip>, val uIds: ArrayList<String>) :  RecyclerView.Adapter<BookedTripAdapter.MyViewHolder>() {

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.booked_trip_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val trip =trips[position]
        val uId = uIds[position]
        holder.setData(trip, position, uId)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var currentTrip: Trip? = null
        private var currentPosition: Int = 0
        private var currentUId: String? = null

        init{
            val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

            itemView.bookedTrip_finish_button.setOnClickListener {
                //GET CURRENT TRIP
                val driverEmail = trips[currentPosition].getDriverEmail()
                val price = trips[currentPosition].getPrice()

                //GET CURRENT USER'S CREDITS
                val userProfileRef = currentUserEmail?.let { it ->
                    db.collection("users").document(it)
                }
                userProfileRef?.get()?.addOnSuccessListener { document ->
                    if (document != null) {
                        var currentUserCredits = document.getDouble("user credits")

                        if (currentUserCredits!! > 0) {
                            //GET TRIP DRIVER'S CREDITS
                            val driverProfileRef = db.collection("users").document(driverEmail)
                            driverProfileRef?.get()?.addOnSuccessListener { document ->
                                if (document != null) {
                                    var driverCred = document.getDouble("user credits")
                                    currentUserCredits -= price!!
                                    driverCred = driverCred?.plus(price!!)

                                    val batch = db.batch()
                                    batch.update(userProfileRef, "user credits", currentUserCredits)
                                    batch.update(driverProfileRef, "user credits", driverCred)
                                    batch.commit()

                                    userProfileRef?.update("booked_trips", FieldValue.arrayRemove(currentUId))
                                    lateinit var passengerAddress: String
                                    userProfileRef?.get()?.addOnSuccessListener { document ->
                                        if (document != null) {
                                            passengerAddress = document.getString("address").toString()
                                            currentUId?.let { it1 ->
                                                db.collection("trips").document(it1).update(
                                                    "passenger_list",
                                                    FieldValue.arrayRemove(passengerAddress)
                                                )
                                            }
                                        } else {
                                            Log.d("Error", "No such document")
                                        }
                                    }?.addOnFailureListener { exception ->
                                        Log.d("Main", "get failed with ", exception)
                                    }


                                    //Deletes the current trip and Uid from the local array for refreshing the page
                                    trips.remove(currentTrip)
                                    uIds.remove(currentUId)
                                    context.showToast("Trip Finished!")
                                    val intent = Intent(context, BookedTrips::class.java)
                                    context.startActivity(intent)
                                }
                            }
                        } else context.showToast("Insufficient Funds!")
                    } else {
                        Log.d("Error", "No such document")
                    }
                }
            }

            itemView.bookedTrip_cancel_button.setOnClickListener {

                val alertDialogForDelete = AlertDialog.Builder(context)
                alertDialogForDelete.setTitle("CANCEL TRIP")
                alertDialogForDelete.setMessage("Are You Sure You Want To Cancel This Trip?")

                alertDialogForDelete.setPositiveButton("YES"){dialog, id ->

                    val userProfileRef = currentUserEmail?.let { it1 ->
                        db.collection("users").document(it1) }
                    userProfileRef?.update("booked_trips", FieldValue.arrayRemove(currentUId))
                    lateinit var passengerAddress: String
                    userProfileRef?.get()?.addOnSuccessListener { document ->
                        if(document != null){
                            passengerAddress = document.getString("address").toString()
                            currentUId?.let { it1 -> db.collection("trips").document(it1).update("passenger_list", FieldValue.arrayRemove(passengerAddress))}
                        } else{
                            Log.d("Error", "No such document")
                        }
                    }?.addOnFailureListener { exception ->
                        Log.d("Main", "get failed with ", exception)
                    }


                    //Deletes the current trip and Uid from the local array for refreshing the page
                    trips.remove(currentTrip)
                    uIds.remove(currentUId)
                    context.showToast("Trip Canceled!")
                    val intent = Intent(context, BookedTrips::class.java)
                    context.startActivity(intent)
                }

                alertDialogForDelete.setNegativeButton("NO"){dialog, id ->
                    dialog.dismiss()
                }
                alertDialogForDelete.show()
            }
        }

        fun setData(trip: Trip?, position: Int, uId: String){

            //gets available seats
            val availableSpace = trip?.getPassengerNo()?.minus(trip.getPassengerList()!!.size)

            itemView.bookedTrip_date.text = trip!!.getDate()
            itemView.bookedTrip_pickup_textValue.text = trip.getRouteOfTrip()
            itemView.bookedTrip_eta_textValue.text = trip.getArrival()
            itemView.bookedTrip_price_textValue.text = trip.getPrice().toString()
            itemView.bookedTrip_noPassengers_textValue.text = availableSpace.toString()
            itemView.bookedTrip_carDetails_textValue.text = trip.getCar()

            this.currentTrip = trip
            this.currentPosition = position
            this.currentUId = uId

        }
    }
}