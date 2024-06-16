package com.example.kakaotest.DataModel.metaRoute

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//complete
@Parcelize
data class End(
    val name:String,
    val lon:Number,
    val lat:Number
) : Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeDouble(lon.toDouble())
        dest.writeDouble(lat.toDouble())
    }
}
