package com.example.kakaotest.DataModel.metaRoute

//complete
data class Itineraries(
    val totalTime:Int,
    val transferCount:Int,
    val totalWalkDistance:Int,
    val totalDistance:Int,
    val totalWalkTime:Int,
    val pathType:Int,
    val fare:Fare,
    val legs:List<Legs>,



)
