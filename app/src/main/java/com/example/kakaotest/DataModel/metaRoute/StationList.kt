package com.example.kakaotest.DataModel.metaRoute

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//complete

data class StationList(
    val index:Int,
    val stationName:String?,
    val lon:Number,
    val lat:Number,
    val stationID:String?
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
        dest.writeInt(index)
        dest.writeString(stationName)
        dest.writeDouble(lon.toDouble())
        dest.writeDouble(lat.toDouble())
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
