package com.example.kakaotest.Utility

import com.example.kakaotest.DataModel.Date
import com.example.kakaotest.DataModel.Destination
import com.example.kakaotest.DataModel.Place
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData

class TravelPlanManager {
    private var travelPlan = TravelPlan()

    fun updatePlan(where: SelectedPlaceData?=null, startDate: Date? = null, endDate: Date ?= null, who: String? = null, transport: String? = null, theme: String? = null, activity: String? = null, destination: ArrayList<SelectedPlaceData>?=null) {
       where?.let{travelPlan.where=it}
        startDate?.let { travelPlan.startDate = it }
        endDate?.let { travelPlan.endDate = it}
        who?.let { travelPlan.who = it }
        transport?.let { travelPlan.transportion = it }
        theme?.let { travelPlan.theme = it }
        activity?.let { travelPlan.activity = it }
        destination?.let{travelPlan.destinations= it}
    }

    fun getPlan(): TravelPlan {
        return travelPlan
    }
}