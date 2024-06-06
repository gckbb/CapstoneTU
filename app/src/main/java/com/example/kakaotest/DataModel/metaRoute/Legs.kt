package com.example.kakaotest.DataModel.metaRoute

//complete
data class Legs(
    val mode:String,
    val sectionTime:Int,
    val distance:Int,
    val start:Start,
    val end:End,
    val steps:List<Steps>,
    val routeColor:String,
    val routeId:String,
    val type:Int,
    val service:Int,
    val passStopList:PassStopList,
    val passShape:PassShape,
    val lane:Lane

)
