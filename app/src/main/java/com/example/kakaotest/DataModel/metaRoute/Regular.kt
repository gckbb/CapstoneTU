package com.example.kakaotest.DataModel.metaRoute

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//complete

data class Regular(
    val totalFare:Int,
    val currency:Currency?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(Currency::class.java.classLoader)
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(totalFare)
        dest.writeParcelable(currency,flags)
    }

    companion object CREATOR : Parcelable.Creator<Regular> {
        override fun createFromParcel(parcel: Parcel): Regular {
            return Regular(parcel)
        }

        override fun newArray(size: Int): Array<Regular?> {
            return arrayOfNulls(size)
        }
    }
}
