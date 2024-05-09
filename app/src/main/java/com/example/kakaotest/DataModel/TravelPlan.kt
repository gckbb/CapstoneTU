package com.example.kakaotest.DataModel

import android.os.Parcel
import android.os.Parcelable
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData

data class TravelPlan(
    var where: SelectedPlaceData?=null,
    var startDate: Date?=null,
    var endDate: Date?=null,
    var who: String?=null,
    var transportion: String?=null,
    var theme: String?=null,
    var activity: String?=null,
    var destinations: List<SelectedPlaceData>?=null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(SelectedPlaceData::class.java.classLoader),
        parcel.readParcelable(Date::class.java.classLoader),
        parcel.readParcelable(Date::class.java.classLoader),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(SelectedPlaceData)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(where, flags)
        parcel.writeParcelable(startDate,flags)
        parcel.writeParcelable(endDate,flags)
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



