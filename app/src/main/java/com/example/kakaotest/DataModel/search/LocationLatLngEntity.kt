package com.example.kakaotest.search

import android.os.Parcel
import android.os.Parcelable
import com.skt.tmap.TMapPoint

data class LocationLatLngEntity(val tpoint: TMapPoint) : Parcelable {

    constructor(parcel: Parcel) : this(
        TMapPoint(parcel.readDouble(), parcel.readDouble())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(tpoint.latitude)
        parcel.writeDouble(tpoint.longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocationLatLngEntity> {
        override fun createFromParcel(parcel: Parcel): LocationLatLngEntity {
            return LocationLatLngEntity(parcel)
        }

        override fun newArray(size: Int): Array<LocationLatLngEntity?> {
            return arrayOfNulls(size)
        }
    }
}
