package com.ncs.awdoor

class DataHolder {

    data class Train(val fare: Int, val name : String , val trainNumber : String, val boardStation : String,
    val deboardStation: String, val departDate : String , val arrivalDate : String)

    data class Flight(val fare : Int , val dept : String, val name :  String, val totalAmt : Int)

    data class Bus(val fare: Int, val company : String , val specification : String , val boardingStation : String, val endStation: String)


}