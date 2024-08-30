package com.example.kakaotest.DataModel.metaRoute

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//complete
@Parcelize
data class End(
    var name:String? = null,
    var lon:Double? = null,
    var lat:Double? = null
) : Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        lon?.let { dest.writeDouble(it.toDouble()) }
        lat?.let { dest.writeDouble(it.toDouble()) }
    }
}
