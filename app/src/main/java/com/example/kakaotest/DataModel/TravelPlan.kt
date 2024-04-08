package com.example.kakaotest.DataModel

data class TravelPlan(
    val where: Place?,
    val startDate: String?,
    val endDate: String?,
    val who: String?,
    val transportion: String?,
    val theme: String?,
    val activity: String?,
    val destinations: List<Place>?
)



