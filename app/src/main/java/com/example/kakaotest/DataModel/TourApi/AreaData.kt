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
    val firstimage: String, //대표이미지(원본)
    val firstimage2:	String, //대표이미지(썸네일)
    val mapx: String, //GPS X좌표
    val mapy: String, //GPS Y좌표
    val mlevel: String, //Map Level
    val addr2: String, //상세주소
    val areacode: String, //지역코드
    val modifiedtime: String, //수정일
    val cpyrhtDivCd: String, //저작권 유형 (Type1:제1유형(출처표시-권장), Type3:제3유형(제1유형+변경금지)
    val booktour: String, //교과서속여행지 여부
    val cat1: String, //대분류
    val sigungucode: String, //시군구코드
    val tel: String, //전화번호
    val title: String, //제목
    val addr1: String, //주소
    val cat2: String, //중분류
    val cat3: String, //소분류
    val contentid: String, //콘텐츠ID
    val contenttypeid: String, //콘텐츠타입ID
    val createdtime: String, //등록일
    val zipcode: String //우편번호
)
