package com.example.kakaotest.DataModel.metaRoute

import com.example.kakaotest.DataModel.tmap.SelectedPlaceData

import android.os.Parcel
import android.os.Parcelable
data class SearchMetaData(
    var metaData: MetaData? = null,
    var pointdata: SelectedPlaceData? = null,
    var time: Int? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(MetaData::class.java.classLoader),
        parcel.readParcelable(SelectedPlaceData::class.java.classLoader),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(metaData, flags)
        parcel.writeParcelable(pointdata, flags)
        time?.let { parcel.writeInt(it.toInt()) }
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