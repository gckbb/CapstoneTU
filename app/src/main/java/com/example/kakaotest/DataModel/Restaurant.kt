package com.example.kakaotest.DataModel

data class RestaurantResponse(
    val response: ResponseData
)

data class ResponseData(
    val header: HeaderData,
    val body: BodyData
)

data class HeaderData(
    val resultCode: String,
    val resultMsg: String
)

data class BodyData(
    val items: ItemsData,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)

data class ItemsData(
    val item: List<Restaurant>
)

data class Restaurant(
    val addr1: String,
    val addr2: String?,
    val areacode: String,
    val booktour: String?,
    val cat1: String,
    val cat2: String,
    val cat3: String,
    val contentid: String,
    val contenttypeid: String,
    val createdtime: String,
    val dist: String,
    val firstimage: String,
    val firstimage2: String,
    val cpyrhtDivCd: String,
    val mapx: String,
    val mapy: String,
    val mlevel: String,
    val modifiedtime: String,
    val sigungucode: String,
    val tel: String,
    val title: String
)
