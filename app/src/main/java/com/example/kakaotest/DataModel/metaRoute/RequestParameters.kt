package com.example.kakaotest.DataModel.metaRoute

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//complete

@Parcelize
data class RequestParameters(
    val busCount: Int,
    val subwayBusCount: Int,
    val expressbusCount: Int,
    val trainCount: Int,
    val airplaneCount: Int,
    val ferryCount: Int,
    val wideareaRouteCount: Int,
    val startX: String?,
    val startY: String?,
    val endX: String?,
    val endY: String?,
    val locale: String?,
    val reqDttm: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(busCount)
        dest.writeInt(subwayBusCount)
        dest.writeInt(expressbusCount)
        dest.writeInt(trainCount)
        dest.writeInt(airplaneCount)
        dest.writeInt(ferryCount)
        dest.writeInt(wideareaRouteCount)
        dest.writeString(startX)
        dest.writeString(startY)
        dest.writeString(endX)
        dest.writeString(endY)
        dest.writeString(locale)
        dest.writeString(reqDttm)


    }

    companion object CREATOR : Parcelable.Creator<RequestParameters> {
        override fun createFromParcel(parcel: Parcel): RequestParameters {
            return RequestParameters(parcel)
        }

        override fun newArray(size: Int): Array<RequestParameters?> {
            return arrayOfNulls(size)
        }
    }
}
