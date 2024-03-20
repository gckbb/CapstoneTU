package com.example.kakaotest.Plan

import android.os.Parcel
import android.os.Parcelable
import com.skt.tmap.TMapPoint

data class FormattedPlace(
    val placeName: String,
    val tpoint: TMapPoint,
    val address: String,
    val totalTime : String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        TMapPoint(
            parcel.readDouble(),
            parcel.readDouble()
        ),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(placeName)
        parcel.writeDouble(tpoint.latitude)
        parcel.writeDouble(tpoint.longitude)
        parcel.writeString(address)
        parcel.writeString(totalTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SelectedPlaceData> {
        override fun createFromParcel(parcel: Parcel): SelectedPlaceData {
            return SelectedPlaceData(
                parcel.readString() ?: "",
                TMapPoint(parcel.readDouble(), parcel.readDouble()),
                parcel.readString() ?: "",

            )
        }

        override fun newArray(size: Int): Array<SelectedPlaceData?> {
            return arrayOfNulls(size)
        }
    }





}
