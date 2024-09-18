package com.example.kakaotest.DataModel.Weather

data class WeatherJsonModel(
    val numOfRows : Int,
    val pageNo : Int,
    val dataType : String,
    val base_date : String,
    val base_time : String,
    val nx : Int,
    val ny : Int
)
