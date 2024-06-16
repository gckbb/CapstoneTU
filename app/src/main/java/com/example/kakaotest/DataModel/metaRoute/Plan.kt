package com.example.kakaotest.DataModel.metaRoute

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize

//complete
data class Plan(
    val itineraries:List<Itineraries>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Itineraries.CREATOR))

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelableList(itineraries,flags)
    }

    companion object CREATOR : Parcelable.Creator<Plan> {
        override fun createFromParcel(parcel: Parcel): Plan {
            return Plan(parcel)
        }

        override fun newArray(size: Int): Array<Plan?> {
            return arrayOfNulls(size)
        }
    }
}
