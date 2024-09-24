package com.example.kakaotest.DataModel.tmap

import android.os.Parcel
import android.os.Parcelable

data class SearchRouteData(
    var pointdata: SelectedPlaceData? = null,
    var time: Number? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(SelectedPlaceData::class.java.classLoader),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(pointdata, flags)
        time?.let { parcel.writeDouble(it as Double) }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchRouteData> {
        override fun createFromParcel(parcel: Parcel): SearchRouteData {
            return SearchRouteData(parcel.readParcelable(SelectedPlaceData::class.java.classLoader),parcel.readDouble())
        }

        override fun newArray(size: Int): Array<SearchRouteData?> {
            return arrayOfNulls(size)
        }
    }
}