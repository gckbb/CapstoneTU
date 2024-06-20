package com.example.kakaotest.Plan

import android.os.Parcel
import android.os.Parcelable

data class PSearchRouteData(
    val pointdata: SelectedPlaceData?,
    var time: Number
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(SelectedPlaceData::class.java.classLoader),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(pointdata, flags)
        parcel.writeDouble(time.toDouble())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PSearchRouteData> {
        override fun createFromParcel(parcel: Parcel): PSearchRouteData {
            return PSearchRouteData(parcel)
        }

        override fun newArray(size: Int): Array<PSearchRouteData?> {
            return arrayOfNulls(size)
        }
    }
}