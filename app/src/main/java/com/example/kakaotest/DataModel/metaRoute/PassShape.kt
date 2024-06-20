package com.example.kakaotest.DataModel.metaRoute

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class PassShape(
    val linestring:String?
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(linestring)
    }

    companion object CREATOR : Parcelable.Creator<PassShape> {
        override fun createFromParcel(parcel: Parcel): PassShape {
            return PassShape(parcel)
        }

        override fun newArray(size: Int): Array<PassShape?> {
            return arrayOfNulls(size)
        }
    }
}
