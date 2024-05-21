package com.example.kakaotest.DataModel.tmap

import android.os.Parcel
import android.os.Parcelable

data class SearchRouteData(
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

    companion object CREATOR : Parcelable.Creator<SearchRouteData> {
        override fun createFromParcel(parcel: Parcel): SearchRouteData {
            return SearchRouteData(parcel)
        }

        override fun newArray(size: Int): Array<SearchRouteData?> {
            return arrayOfNulls(size)
        }
    }
}