package com.example.kakaotest.DataModel.metaRoute

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize

//complete
data class Itineraries(
    val totalTime:Int,
    val transferCount:Int,
    val totalWalkDistance:Int,
    val totalDistance:Int,
    val totalWalkTime:Int,
    val pathType:Int,
    val fare:Fare?,
    val legs:List<Legs>?,



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
        parcel.writeInt(totalTime)
        parcel.writeInt(transferCount)
        parcel.writeInt(totalWalkDistance)
        parcel.writeInt(totalDistance)
        parcel.writeInt(totalWalkTime)
        parcel.writeInt(pathType)
        parcel.writeParcelable(fare, flags)
        parcel.writeParcelableList(legs, flags)
    }
}

