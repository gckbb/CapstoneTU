package com.example.kakaotest.DataModel.metaRoute

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class MetaData(
    val requestParameters:RequestParameters,
    val plan:Plan
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(RequestParameters::class.java.classLoader)!!,
        parcel.readParcelable(Plan::class.java.classLoader)!!
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(requestParameters, flags)
        parcel.writeParcelable(plan, flags)
    }

    companion object CREATOR : Parcelable.Creator<MetaData> {
        override fun createFromParcel(parcel: Parcel): MetaData {
            return MetaData(parcel)
        }

        override fun newArray(size: Int): Array<MetaData?> {
            return arrayOfNulls(size)
        }
    }
}
