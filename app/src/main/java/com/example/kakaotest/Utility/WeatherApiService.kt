package com.example.kakaotest.Utility.tmap

import com.example.kakaotest.DataModel.Weather.WeatherJsonModel
import com.example.kakaotest.DataModel.Weather.*
import com.example.kakaotest.DataModel.metaRoute.*
import com.example.kakaotest.DataModel.tmap.FeatureCollection
import com.example.kakaotest.Utility.Url
import com.example.kakaotest.response.address.AddressInfoResponse
import com.example.kakaotest.response.search.SearchResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.HashMap

interface WeatherApiService {


    @GET("getVilageFcst?serviceKey=0SBtabn6sDwk3g4qK+L8Cr4oK2Qlci8pRCgCvKZ2vQ2UjAweAeJ0N8yZddUN9wNHH4YI49XCzx3nxxbiNJ101g==")
    fun getRoute(@Query("numOfRows") num_of_rows : Int,   // 한 페이지 경과 수
            @Query("pageNo") page_no : Int,          // 페이지 번호
            @Query("dataType") data_type : String,   // 응답 자료 형식
            @Query("base_date") base_date : String,  // 발표 일자
            @Query("base_time") base_time : String,  // 발표 시각
            @Query("nx") nx : String,                // 예보지점 X 좌표
            @Query("ny") ny : String
    ): Call<WEATHER>


}