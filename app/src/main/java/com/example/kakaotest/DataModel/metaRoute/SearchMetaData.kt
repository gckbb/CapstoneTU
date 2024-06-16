package com.example.kakaotest.DataModel.metaRoute

import com.example.kakaotest.DataModel.tmap.SelectedPlaceData

import android.os.Parcel
import android.os.Parcelable
data class SearchMetaData(
    val metaData: MetaData?,
    val pointdata: SelectedPlaceData?,
    var time: Number
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(MetaData::class.java.classLoader),
        parcel.readParcelable(SelectedPlaceData::class.java.classLoader),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(metaData, flags)
        parcel.writeParcelable(pointdata, flags)
        parcel.writeDouble(time.toDouble())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchMetaData> {
        override fun createFromParcel(parcel: Parcel): SearchMetaData {
            return SearchMetaData(parcel)
        }

        override fun newArray(size: Int): Array<SearchMetaData?> {
            return arrayOfNulls(size)
        }
    }
}