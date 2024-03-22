package com.example.kakaotest.Plan

import android.os.Parcel
import android.os.Parcelable
import com.skt.tmap.TMapPoint

data class SelectedPlace(  val name: String,
                           val address: String,
                           val tmapPoint: TMapPoint
):
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        TMapPoint(
            parcel.readDouble(),
            parcel.readDouble()
        )
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeDouble(tmapPoint.latitude)
        parcel.writeDouble(tmapPoint.longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SelectedPlace> {
        override fun createFromParcel(parcel: Parcel): SelectedPlace {
            return SelectedPlace(parcel)
        }

        override fun newArray(size: Int): Array<SelectedPlace?> {
            return arrayOfNulls(size)
        }
    }
}

