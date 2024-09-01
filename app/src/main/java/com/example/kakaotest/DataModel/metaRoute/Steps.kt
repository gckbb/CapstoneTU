package com.example.kakaotest.DataModel.metaRoute

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//complete

data class Steps(
    var streetName:String? = null,
    var distance:Int? = null,
    var description:String? = null,
    var linestring:String? = null
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
        distance?.let { dest.writeInt(it) }
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
