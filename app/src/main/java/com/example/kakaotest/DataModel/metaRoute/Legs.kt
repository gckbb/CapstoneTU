package com.example.kakaotest.DataModel.metaRoute

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.util.ArrayList

//complete


data class Legs(
    var mode: String? = null,
    var sectionTime:Int? = null,
    var distance:Int? = null,
    var start:Start? = null,
    var end:End? = null,

    var route:String? = null,

    var steps: List<Steps>? = null,
    var routeColor: String? = null,
    var routeId: String? = null,
    var type:Int? = null,
    var service:Int? = null,
    var passStopList:PassStopList? = null,
    var passShape:PassShape? = null,
    var lane:Lane? = null

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
        sectionTime?.let { parcel.writeInt(it) }
        distance?.let { parcel.writeInt(it) }
        parcel.writeParcelable(start, flags)
        parcel.writeParcelable(end, flags)

        parcel.writeString(route)

        parcel.writeParcelableList(steps, flags)
        parcel.writeString(routeColor)
        parcel.writeString(routeId)
        type?.let { parcel.writeInt(it) }
        service?.let { parcel.writeInt(it) }
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

