package com.example.kakaotest.DataModel

import java.io.Serializable

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
    val addr1: String,  //주소
    val addr2: String?, //상세주소
    val areacode: String,   //지역코드
    val booktour: String?,  //교과서속여행지 여부
    val cat1: String,    //대분류
    val cat2: String,   //중분류
    val cat3: String,   //소분류
    val contentid: String,  //콘첸츠ID
    val contenttypeid: String,  //콘텐츠타입ID
    val createdtime: String,    //등록일
    val dist: String,   //거리
    val firstimage: String, //대표이미지(원본)
    val firstimage2: String,    //대표이미지(섬네일)
    val cpyrhtDivCd: String,    //저작권 유형 (Type1:제1유형(출처표시-권장), Type3:제3유형(제1유형+변경금지)
    val mapx: String,   //x좌표
    val mapy: String,   //y좌표
    val mlevel: String, //맵 레벨
    val modifiedtime: String,   //수정일
    val sigungucode: String,    //시군코드
    val tel: String,    //전화번호
    val title: String,   //제목
    val rnum: String,
    val code: String,
    val name: String
): Serializable
