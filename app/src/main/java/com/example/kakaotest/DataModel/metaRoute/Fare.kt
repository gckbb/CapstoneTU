package com.example.kakaotest.DataModel.metaRoute

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//complete

data class Fare(
    val regular:Regular?
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable(Regular::class.java.classLoader)) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(regular, flags)
    }

    companion object CREATOR : Parcelable.Creator<Fare> {
        override fun createFromParcel(parcel: Parcel): Fare {
            return Fare(parcel)
        }

        override fun newArray(size: Int): Array<Fare?> {
            return arrayOfNulls(size)
        }
    }
}
