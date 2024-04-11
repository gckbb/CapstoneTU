package com.example.kakaotest.DataModel

import android.os.Parcel
import android.os.Parcelable

data class TravelPlan(
    val where: Place?,
    val startDate: String?,
    val endDate: String?,
    val who: String?,
    val transportion: String?,
    val theme: String?,
    val activity: String?,
    val destinations: List<Place>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Place::class.java.classLoader),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Place)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(where, flags)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeString(who)
            parcel.writeString(transportion)
        parcel.writeString(theme)
        parcel.writeString(activity)
        parcel.writeTypedList(destinations)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TravelPlan> {
        override fun createFromParcel(parcel: Parcel): TravelPlan {
            return TravelPlan(parcel)
        }

        override fun newArray(size: Int): Array<TravelPlan?> {
            return arrayOfNulls(size)
        }
    }
}



