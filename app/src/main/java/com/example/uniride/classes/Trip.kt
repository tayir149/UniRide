package com.example.uniride.classes

class Trip (val driver: UserAccount,val dateOfTrip: String, val eta: String, val route: String, val priceOfTrip: Double, val carDetails: String, val numberOfPassenger: Int, var listOfPickUps: Array<Pickup>) {

}
