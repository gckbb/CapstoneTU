package com.example.kakaotest.search

import android.os.Parcel
import android.os.Parcelable
import com.skt.tmap.TMapPoint

data class SearchResultEntity(
    val address: String,
    val name: String,
    val locationLatLng: LocationLatLngEntity
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(LocationLatLngEntity::class.java.classLoader) ?: LocationLatLngEntity(
            TMapPoint(0.0, 0.0)
        )
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(address)
        parcel.writeString(name)
        parcel.writeParcelable(locationLatLng, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchResultEntity> {
        override fun createFromParcel(parcel: Parcel): SearchResultEntity {
            return SearchResultEntity(parcel)
        }

        override fun newArray(size: Int): Array<SearchResultEntity?> {
            return arrayOfNulls(size)
        }
    }
}
