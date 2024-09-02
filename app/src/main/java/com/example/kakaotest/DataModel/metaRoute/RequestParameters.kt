package com.example.kakaotest.DataModel.metaRoute

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//complete

@Parcelize
data class RequestParameters(
    var busCount: Int? = null,
    var subwayBusCount: Int? = null,
    var expressbusCount: Int? = null,
    var trainCount: Int? = null,
    var airplaneCount: Int? = null,
    var ferryCount: Int? = null,
    var wideareaRouteCount: Int? = null,
    var startX: String? = null,
    var startY: String? = null,
    var endX: String? = null,
    var endY: String? = null,
    var locale: String? = null,
    var reqDttm: String? = null
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
        busCount?.let { dest.writeInt(it) }
        subwayBusCount?.let { dest.writeInt(it) }
        expressbusCount?.let { dest.writeInt(it) }
        trainCount?.let { dest.writeInt(it) }
        airplaneCount?.let { dest.writeInt(it) }
        ferryCount?.let { dest.writeInt(it) }
        wideareaRouteCount?.let { dest.writeInt(it) }
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
