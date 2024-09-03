
package com.example.kakaotest.DataModel.tmap


import android.os.Parcel
import android.os.Parcelable
import com.skt.tmap.TMapPoint


data class SelectedPlaceData(
    var placeName: String? = null,
    var tpoint: TMapPoint? = null, // TMapPoint는 Parcelable이어야 함
    var address: String? = null,
    var stayDuration: Int? = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        TMapPoint(
            parcel.readDouble(),
            parcel.readDouble()
        ),
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(placeName)
        tpoint?.let { parcel.writeDouble(it.latitude) }
        tpoint?.let { parcel.writeDouble(it.longitude) }
        parcel.writeString(address)
        stayDuration?.let { parcel.writeInt(it) }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SelectedPlaceData> {
        override fun createFromParcel(parcel: Parcel): SelectedPlaceData {
            return SelectedPlaceData(
                parcel.readString() ?: "",
                TMapPoint(parcel.readDouble(), parcel.readDouble()),
                parcel.readString() ?: "",
                parcel.readInt()
            )
        }

        override fun newArray(size: Int): Array<SelectedPlaceData?> {
            return arrayOfNulls(size)
        }
    }





}
