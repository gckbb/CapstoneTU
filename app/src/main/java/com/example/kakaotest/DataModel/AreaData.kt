package com.example.kakaotest.DataModel

data class AreaDataResponse(
    val response: AreaResponseData
)

data class AreaResponseData(
    val header: AreaHeaderData,
    val body: AreaBodyData
)

data class AreaHeaderData(
    val resultCode: String,
    val resultMsg: String
)

data class AreaBodyData(
    val items: AreaItemsData
)

data class AreaItemsData(
    val item: List<AreaData>
)

data class AreaData(
    val rnum: String, //일련번호
    val code: String, //코드 : 대,중,소분류코드
    val name: String //코드명 : 대,중,소분류코드명
)
