package com.example.uniride.classes

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class Trip(driverName: String?, dateOfTrip: String?, eta: String?, route: String?, priceOfTrip: Double, carDetails: String?, numberOfPassenger: Int?, userEmail: String?) {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val tripDriver= driverName
    private val date = dateOfTrip
    private val timeArrival = eta
    private val routeOfTrip = route
    private val price = priceOfTrip
    private val numberOfPassengerOn = numberOfPassenger
    private val car = carDetails
    private val userEmailAddress = userEmail

    fun getDate():String{
        return date.toString()
    }
    fun getArrival():String{
        return timeArrival.toString()
    }
    fun getRoute():String{
        return routeOfTrip.toString()
    }
    fun getPrice():Double{
        return price
    }
    fun getPassengerNo():Int?{
        return numberOfPassengerOn
    }
    fun getCar():String{
        return car.toString()
    }

    fun saveTripToDatabase(){
        val trip = mapOf("trip_driver" to tripDriver, "date" to date,"estimated_arrival_time" to timeArrival, "route" to routeOfTrip, "price" to price,
                                            "number_of_passengers" to numberOfPassengerOn, "car_detail" to car)

        userEmailAddress?.let {
            db.collection("users").document(it).collection("created_trips").add(trip)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    // else if successful
                    Log.d("testCreatingTrip","Successfully created trip")
                }
                .addOnFailureListener(){
                    Log.d("testCreatingTrip", "Failed to create trip: ${it.message}")
                }
        }
        }
    }

