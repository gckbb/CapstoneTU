package com.example.kakaotest.Plan

import android.os.Parcel
import android.os.Parcelable
import java.util.LinkedList

data class DRouteData(
    val totalTime: Number,
    val dayRoute: LinkedList<PSearchRouteData>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readArrayList(PSearchRouteData::class.java.classLoader) as LinkedList<PSearchRouteData>?
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(totalTime as Double)
        parcel.writeList(dayRoute)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DRouteData> {
        override fun createFromParcel(parcel: Parcel): DRouteData {
            return DRouteData(parcel)
        }

        override fun newArray(size: Int): Array<DRouteData?> {
            return arrayOfNulls(size)
        }
    }
}