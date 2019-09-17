package com.example.uniride.classes

import java.math.BigDecimal

class UserAccount (firstName:String?,lastName:String?,address:String?){
    private val userFirstName = firstName
    private val userLastName = lastName
    private val userAddress = address
    //get userURD from database
    //urd = uniride dollar
    var userURD : BigDecimal? = BigDecimal.ZERO
    fun printUser(){
        println(userFirstName+userLastName+":"+userAddress)
        println("You have: "+userURD)
    }
}
//testing
//fun main(args: Array<String>){
//    val obj = UserAccount("Jekk","Grata","my address 01")
//    obj.printUser()
//}