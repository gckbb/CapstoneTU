package com.example.kakaotest.DataModel

import android.os.Parcel
import android.os.Parcelable
import com.example.kakaotest.DataModel.tmap.SearchData
import com.skt.tmap.TMapPoint

data class Place( //여행하는 도시나 지역의 장소
    val placeName: String,
    val tpoint: TMapPoint, // TMapPoint는 Parcelable이어야 함
    val address: String
) :   Parcelable {

    constructor(parcel: Parcel) : this(
    parcel.readString() ?: "",
    TMapPoint(
        parcel.readDouble(),
        parcel.readDouble()
    ),
    parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(placeName)
        parcel.writeDouble(tpoint.latitude)
        parcel.writeDouble(tpoint.longitude)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Place> {
        override fun createFromParcel(parcel: Parcel): Place {
            return Place(parcel)
        }

        override fun newArray(size: Int): Array<Place?> {
            return arrayOfNulls(size)
        }
    }
}

