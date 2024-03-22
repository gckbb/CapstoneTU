package com.example.kakaotest.Plan

import android.os.Parcel
import android.os.Parcelable

data class TotalRouteData(
    val routeDataList: List<RouteData>
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(RouteData.CREATOR) ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(routeDataList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TotalRouteData> {
        override fun createFromParcel(parcel: Parcel): TotalRouteData {
            return TotalRouteData(parcel)
        }

        override fun newArray(size: Int): Array<TotalRouteData?> {
            return arrayOfNulls(size)
        }
    }
}