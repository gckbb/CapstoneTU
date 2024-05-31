package com.example.kakaotest.DataModel

import android.os.Parcel
import android.os.Parcelable
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData

data class TravelPlan(
    var where: Place?=null,
    var startDate: Date?=null,
    var endDate: Date?=null,
    var who: String?=null,
    var transportion: String?=null,
    var theme: String?=null,
    var activityTime: Int?=null,
    var startTime: Time?=null,
    var restaurant: String?=null,
    var destinations: List<SearchRouteData>?=null
) : Parcelable {



    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Place::class.java.classLoader),
        parcel.readParcelable(Date::class.java.classLoader),
        parcel.readParcelable(Date::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readParcelable(Time::class.java.classLoader),
        parcel.readString(),
        parcel.createTypedArrayList(SearchRouteData)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(where, flags)
        parcel.writeParcelable(startDate,flags)
        parcel.writeParcelable(endDate,flags)
        parcel.writeString(who)
            parcel.writeString(transportion)
        parcel.writeString(theme)
        activityTime?.let { parcel.writeInt(it) }
        parcel.writeParcelable(startTime, flags)
        parcel.writeString(restaurant)
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



