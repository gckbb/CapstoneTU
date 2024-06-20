package com.example.kakaotest.DataModel.metaRoute

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize

//complete

data class PassStopList(
    val stationList:List<StationList>?,

) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(StationList.CREATOR)) {
    }

    override fun describeContents(): Int {
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelableList(stationList,flags)
    }

    companion object CREATOR : Parcelable.Creator<PassStopList> {
        override fun createFromParcel(parcel: Parcel): PassStopList {
            return PassStopList(parcel)
        }

        override fun newArray(size: Int): Array<PassStopList?> {
            return arrayOfNulls(size)
        }
    }
}
