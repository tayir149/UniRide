package com.example.uniride

import android.location.Address
import java.sql.Time

import java.util.*

class Trip{
    lateinit var homeAddress: Address
    lateinit var uniAddress: Address
    lateinit var driver: Driver
    lateinit var date: Date
    lateinit var estimatedArrivalTime: Time
    var price = 0.00
    lateinit var carMakeModel: String
    lateinit var listOfPickups: Array<Pickup>
    lateinit var listOfPassenger: Array<Passenger>

    fun setHomeAddress(address: String){
        homeAddress.setAddressLine()
    }







}

private fun Address.setAddressLine() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
