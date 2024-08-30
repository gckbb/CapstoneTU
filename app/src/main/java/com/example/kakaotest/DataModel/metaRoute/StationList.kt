package com.example.kakaotest.DataModel.metaRoute

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//complete

data class StationList(
    var index:Int? = null,
    var stationName:String? = null,
    var lon:Double? = null,
    var lat:Double? = null,
    var stationID:String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        index?.let { dest.writeInt(it) }
        dest.writeString(stationName)
        lon?.let { dest.writeDouble(it.toDouble()) }
        lat?.let { dest.writeDouble(it.toDouble()) }
        dest.writeString(stationID)
    }

    companion object CREATOR : Parcelable.Creator<StationList> {
        override fun createFromParcel(parcel: Parcel): StationList {
            return StationList(parcel)
        }

        override fun newArray(size: Int): Array<StationList?> {
            return arrayOfNulls(size)
        }
    }
}
