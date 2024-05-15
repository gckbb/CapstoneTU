package com.example.kakaotest.Utility

import com.example.kakaotest.DataModel.Date
import com.example.kakaotest.DataModel.Place
import com.example.kakaotest.DataModel.Time
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.tmap.SearchRouteData

class TravelPlanManager {
    private var travelPlan = TravelPlan()

    fun updatePlan(
        where: Place? = null,
        startDate: Date? = null,
        endDate: Date? = null,
        who: String? = null,
        transport: String? = null,
        theme: String? = null,
        activityTime: Int? = null,
        startTime:Time? = null,
        restaurant : String? = null,
        destination: List<SearchRouteData>? = null
    ) {
        where?.let { travelPlan.where = it }
        startDate?.let { travelPlan.startDate = it }
        endDate?.let { travelPlan.endDate = it }
        who?.let { travelPlan.who = it }
        transport?.let { travelPlan.transportion = it }
        theme?.let { travelPlan.theme = it }
        activityTime?.let { travelPlan.activityTime = it }
        startTime?.let { travelPlan.startTime = it }
        restaurant?. let{travelPlan.restaurant = it}
        destination?.let { travelPlan.destinations = it }
    }

    fun getPlan(): TravelPlan {
        return travelPlan
    }
}
