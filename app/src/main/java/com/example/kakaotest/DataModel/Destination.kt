package com.example.kakaotest.DataModel

data class Destination( //여행정보
    val cityName :String,
    val placesToVistio : List<Place>,
    val itinerary : List<ItineraryItem>
)