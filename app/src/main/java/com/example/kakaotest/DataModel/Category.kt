package com.example.kakaotest.DataModel

import java.io.Serializable

data class CategoryResponse(
    val response: CategoryResponseData
)

data class CategoryResponseData(
    val header: CategoryHeaderData,
    val body: CategoryBodyData
)

data class CategoryHeaderData(
    val resultCode: String,
    val resultMsg: String
)

data class CategoryBodyData(
    val items: CategoryData,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)

data class CategoryData(
    val item: List<Category>
)

data class Category(
    val rnum: String, //일련번호
    val code: String, //코드 : 대,중,소분류코드
    val name: String, //코드명 : 대,중,소분류코드명
): Serializable
