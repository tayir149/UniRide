package com.example.uniride.classes

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList

class Trip(driverName: String?, dateOfTrip: String?, eta: String?,
                route: String?, priceOfTrip: Double?,carDetails: String?,
                numberOfPassenger: Int?,userEmail: String?,
                passengers: ArrayList<String>? = null) {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val tripDriver= driverName
    private val date = dateOfTrip
    private val timeArrival = eta
    private val routeOfTrip = route
    private val price = priceOfTrip
    private val numberOfPassengerOn = numberOfPassenger
    private val car = carDetails
    private val userEmailAddress = userEmail
    private var passengerList = passengers

    fun passengerBookedTrip(passengerEmail: String?){
        passengerEmail?.let {
            db.collection("users").document(it).get().addOnSuccessListener { document ->
                val firstName = document.getString("first name")
                val lastName = document.getString("last name")
                val passengerName = "$firstName $lastName"
                passengerList?.add(passengerName)
            }
        }
    }

    fun getPassengerList(): ArrayList<String>? {
        return passengerList
    }

    fun getTripDriver():String{
        return tripDriver.toString()
    }
    fun getDate():String{
        return date.toString()
    }
    fun getArrival():String{
        return timeArrival.toString()
    }
    fun getRouteOfTrip():String{
        return routeOfTrip.toString()
    }
    fun getPrice(): Double? {
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
                                            "number_of_passengers" to numberOfPassengerOn, "car_detail" to car, "user_email" to userEmailAddress, "passenger_list" to ArrayList<String>())

            db.collection("trips").add(trip)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                }
                .addOnFailureListener(){
                    Log.d("testCreatingTrip", "Failed to create trip: ${it.message}")
                }

        }
    }

