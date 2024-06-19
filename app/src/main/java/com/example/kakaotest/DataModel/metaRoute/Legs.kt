package com.example.kakaotest.DataModel.metaRoute

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.util.ArrayList

//complete


data class Legs(
    val mode: String?,
    val sectionTime:Int,
    val distance:Int,
    val start:Start?,
    val end:End?,
    val route:String?,
    val steps: List<Steps>?,
    val routeColor: String?,
    val routeId: String?,
    val type:Int,
    val service:Int,
    val passStopList:PassStopList?,
    val passShape:PassShape?,
    val lane:Lane?

) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(Start::class.java.classLoader),
        parcel.readParcelable(End::class.java.classLoader),
        parcel.readString(),
        parcel.createTypedArrayList(Steps.CREATOR),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(PassStopList::class.java.classLoader),
        parcel.readParcelable(PassShape::class.java.classLoader),
        parcel.readParcelable(Lane::class.java.classLoader)
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mode)
        parcel.writeInt(sectionTime)
        parcel.writeInt(distance)
        parcel.writeParcelable(start, flags)
        parcel.writeParcelable(end, flags)
        parcel.writeString(route)
        parcel.writeParcelableList(steps, flags)
        parcel.writeString(routeColor)
        parcel.writeString(routeId)
        parcel.writeInt(type)
        parcel.writeInt(service)
        parcel.writeParcelable(passStopList, flags)
        parcel.writeParcelable(passShape, flags)
        parcel.writeParcelable(lane, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Legs> {
        override fun createFromParcel(parcel: Parcel): Legs {
            return Legs(parcel)
        }

        override fun newArray(size: Int): Array<Legs?> {
            return arrayOfNulls(size)
        }
    }
}

