package com.example.kakaotest.DataModel

import com.skt.tmap.TMapPoint

data class Place( //여행하는 도시나 지역의 장소
    val placeName: String,
    val tpoint: TMapPoint, // TMapPoint는 Parcelable이어야 함
    val address: String
)