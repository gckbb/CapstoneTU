package com.example.kakaotest.DataModel

data class DailyDestination(
    val date: String,
    val destination: Destination,
    val itinerary: List<ItineraryItem>
)
