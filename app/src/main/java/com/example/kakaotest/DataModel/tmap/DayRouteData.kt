package com.example.kakaotest.DataModel.tmap

import android.os.Parcel
import android.os.Parcelable
import java.util.LinkedList

data class DayRouteData(
    val totalTime: Number,
    val dayRoute: LinkedList<SearchRouteData>?
)
