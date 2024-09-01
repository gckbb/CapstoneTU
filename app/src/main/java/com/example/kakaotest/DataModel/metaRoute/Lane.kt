package com.example.kakaotest.DataModel.metaRoute

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//complete

data class Lane(
    var routeColor:String? = null,
    var route:String? = null,
    var routeId:String? = null,
    var service:Int? = null,
    var type:Int? = null
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
        service?.let { dest.writeInt(it) }
        type?.let { dest.writeInt(it) }
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
