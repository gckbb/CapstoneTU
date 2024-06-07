package com.example.kakaotest.DataModel

import android.os.Parcel
import android.os.Parcelable
import com.skt.tmap.TMapPoint
import java.io.Serializable
data class ParcelableTMapPoint(val latitude: Double, val longitude: Double) : Parcelable, Serializable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableTMapPoint> {
        override fun createFromParcel(parcel: Parcel): ParcelableTMapPoint {
            return ParcelableTMapPoint(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableTMapPoint?> {
            return arrayOfNulls(size)
        }
    }
}

fun TMapPoint.toParcelableTMapPoint(): ParcelableTMapPoint {
    return ParcelableTMapPoint(latitude, longitude)
}

