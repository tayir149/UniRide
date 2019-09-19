package com.example.uniride.classes

import com.google.firebase.firestore.FirebaseFirestore

class Trip (driverName: String, dateOfTrip: String, eta: String, route: String, priceOfTrip: Double, carDetails: String, numberOfPassenger: Int) {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val tripDriver= driverName
    private val date = dateOfTrip
    private val timeArrival = eta
    private val routeOfTrip = route
    private val price = priceOfTrip
    private val numberOfPassengerOn = numberOfPassenger
    private val car = carDetails


}
