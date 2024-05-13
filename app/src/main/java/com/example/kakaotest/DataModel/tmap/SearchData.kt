package com.example.kakaotest.DataModel.tmap

import android.os.Parcel
import android.os.Parcelable
import com.skt.tmap.TMapPoint


data class SearchData(val id: String, val tpoint: TMapPoint, val address: String) :
        Parcelable {

        constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            TMapPoint(
                parcel.readDouble(),
                parcel.readDouble()
            ),
            parcel.readString() ?: ""
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeDouble(tpoint.latitude)
            parcel.writeDouble(tpoint.longitude)
            parcel.writeString(address)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<SearchData> {
            override fun createFromParcel(parcel: Parcel): SearchData {
                return SearchData(parcel)
            }

            override fun newArray(size: Int): Array<SearchData?> {
                return arrayOfNulls(size)
            }
        }
        }