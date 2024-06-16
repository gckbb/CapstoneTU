package com.example.kakaotest.DataModel.metaRoute

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//complete

data class Steps(
    val streetName:String?,
    val distance:Int,
    val description:String?,
    val linestring:String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(streetName)
        dest.writeInt(distance)
        dest.writeString(description)
        dest.writeString(linestring)
    }

    companion object CREATOR : Parcelable.Creator<Steps> {
        override fun createFromParcel(parcel: Parcel): Steps {
            return Steps(parcel)
        }

        override fun newArray(size: Int): Array<Steps?> {
            return arrayOfNulls(size)
        }
    }
}
