package com.example.kakaotest.DataModel.tmap

import android.os.Parcel
import android.os.Parcelable
import java.util.LinkedList

data class DayRouteData(
    val totalTime: Number,
    val dayRoute: LinkedList<SearchRouteData>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readArrayList(SearchRouteData::class.java.classLoader) as LinkedList<SearchRouteData>?
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(totalTime as Double)
        parcel.writeList(dayRoute)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DayRouteData> {
        override fun createFromParcel(parcel: Parcel): DayRouteData {
            return DayRouteData(parcel)
        }

        override fun newArray(size: Int): Array<DayRouteData?> {
            return arrayOfNulls(size)
        }
    }
}