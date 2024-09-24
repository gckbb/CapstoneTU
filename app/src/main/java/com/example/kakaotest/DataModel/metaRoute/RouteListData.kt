package com.example.kakaotest.DataModel.metaRoute

import android.os.Parcel
import android.os.Parcelable
import com.example.kakaotest.DataModel.tmap.SearchRouteData



data class RouteListData(
    var routeList : ArrayList<SearchRouteData>? = null
)
