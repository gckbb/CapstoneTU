package com.example.kakaotest.DataModel

import android.os.Parcel
import android.os.Parcelable
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.skt.tmap.TMapPoint
import java.security.Timestamp

data class TravelPlan (
    var date : List<Timestamp>?= null,
    var area : List<area>? =null,
    var SelectedPlaceData : List<SelectedPlaceData>? = null

)

data class area(
    val placeName: String,
    val tpoint: TMapPoint, // TMapPoint는 Parcelable이어야 함
    val address: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        TMapPoint(
            parcel.readDouble(),
            parcel.readDouble()
        ),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(placeName)
        parcel.writeDouble(tpoint.latitude)
        parcel.writeDouble(tpoint.longitude)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SelectedPlaceData> {
        override fun createFromParcel(parcel: Parcel): SelectedPlaceData {
            return SelectedPlaceData(
                parcel.readString() ?: "",
                TMapPoint(parcel.readDouble(), parcel.readDouble()),
                parcel.readString() ?: ""
            )
        }

        override fun newArray(size: Int): Array<SelectedPlaceData?> {
            return arrayOfNulls(size)
        }
    }
}




