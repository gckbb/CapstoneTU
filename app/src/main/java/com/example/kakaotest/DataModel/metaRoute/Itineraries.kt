package com.example.kakaotest.DataModel.metaRoute

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize

//complete
data class Itineraries(
    var totalTime:Int? = null,
    var transferCount:Int? = null,
    var totalWalkDistance:Int? = null,
    var totalDistance:Int? = null,
    var totalWalkTime:Int? = null,
    var pathType:Int? = null,
    var fare:Fare? = null,
    var legs:List<Legs>? = null,



) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(Fare::class.java.classLoader),
        parcel.createTypedArrayList(Legs.CREATOR)
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Itineraries> {
        override fun createFromParcel(parcel: Parcel): Itineraries {
            return Itineraries(parcel)
        }

        override fun newArray(size: Int): Array<Itineraries?> {
            return arrayOfNulls(size)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        totalTime?.let { parcel.writeInt(it) }
        transferCount?.let { parcel.writeInt(it) }
        totalWalkDistance?.let { parcel.writeInt(it) }
        totalDistance?.let { parcel.writeInt(it) }
        totalWalkTime?.let { parcel.writeInt(it) }
        pathType?.let { parcel.writeInt(it) }
        parcel.writeParcelable(fare, flags)
        parcel.writeParcelableList(legs, flags)
    }
}

