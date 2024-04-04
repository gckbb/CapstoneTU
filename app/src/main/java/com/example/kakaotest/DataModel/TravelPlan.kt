package com.example.kakaotest.DataModel

import android.os.Parcel
import android.os.Parcelable
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.skt.tmap.TMapPoint
import java.security.Timestamp

data class TravelPlan (
    val tripId : String,
    val startDate : String,
    val endDate : String,
    val destinations : List<DailyDestination>

)



