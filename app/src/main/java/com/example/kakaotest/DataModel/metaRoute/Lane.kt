package com.example.kakaotest.DataModel.metaRoute

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//complete

data class Lane(
    val routeColor:String?,
    val route:String?,
    val routeId:String?,
    val service:Int,
    val type:Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(routeColor)
        dest.writeString(route)
        dest.writeString(routeId)
        dest.writeInt(service)
        dest.writeInt(type)
    }

    companion object CREATOR : Parcelable.Creator<Lane> {
        override fun createFromParcel(parcel: Parcel): Lane {
            return Lane(parcel)
        }

        override fun newArray(size: Int): Array<Lane?> {
            return arrayOfNulls(size)
        }
    }
}
