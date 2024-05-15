package com.example.kakaotest.DataModel

import android.os.Parcel
import android.os.Parcelable

data class UserData(
    var id: String? = null,
    var pw: String? = null,
    var name : String? = null,
    var email : String? = null,
    var phone : String? = null,
    var signtime: Long? = null,
    var socialLogin : Boolean? = false,
    var postCount: Int? = null,
    var writtenPosts: List<PostData>? = null, // 작성한 게시글 리스트
    var savedPosts: List<PostData>? = null, // 저장한 게시글 리스트
    var travelPlanCount: Int? = 0, // 여행 일정 개수
    var travelPlans : List<TravelPlan>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(pw)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeValue(signtime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserData> {
        override fun createFromParcel(parcel: Parcel): UserData {
            return UserData(parcel)
        }

        override fun newArray(size: Int): Array<UserData?> {
            return arrayOfNulls(size)
        }
    }
}

