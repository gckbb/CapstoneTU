package com.example.kakaotest.DataModel

import android.os.Parcel
import android.os.Parcelable

data class Date(
    var date : String?,
    var year : Int?,
    var month :Int?,
    var day : Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt()!!,
        parcel.readInt()!!,
        parcel.readInt()!!,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeInt(year!!)
        parcel.writeInt(month!!)
        parcel.writeInt(day!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Date> {
        override fun createFromParcel(parcel: Parcel): Date {
            return Date(parcel)
        }

        override fun newArray(size: Int): Array<Date?> {
            return arrayOfNulls(size)
        }
    }
}
