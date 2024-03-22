package com.example.kakaotest.Plan

import android.os.Parcel
import android.os.Parcelable

data class RouteData(
    val date: Int,
    val totalTime: String,
    val selectedPlaceList: List<SelectedPlace>): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        mutableListOf<SelectedPlace>().apply {
            parcel.readList(this, SelectedPlace::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(date)
        parcel.writeString(totalTime)
        parcel.writeList(selectedPlaceList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RouteData> {
        override fun createFromParcel(parcel: Parcel): RouteData {
            return RouteData(parcel)
        }

        override fun newArray(size: Int): Array<RouteData?> {
            return arrayOfNulls(size)
        }
    }
}