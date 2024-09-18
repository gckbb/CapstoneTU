package com.example.kakaotest.Utility

import com.example.kakaotest.DataModel.Weather.WEATHER
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

// 결과 xml 파일에 접근해서 정보 가져오기
interface WeatherInterface {
    // getUltraSrtFcst : 초단기 예보 조회 + 인증키

    @GET("getVilageFcst?serviceKey=0SBtabn6sDwk3g4qK%2BL8Cr4oK2Qlci8pRCgCvKZ2vQ2UjAweAeJ0N8yZddUN9wNHH4YI49XCzx3nxxbiNJ101g%3D%3D")

    fun getWeather(
        @Query("numOfRows") num_of_rows: Int,   // 한 페이지 경과 수
        @Query("pageNo") page_no: Int,          // 페이지 번호
        @Query("dataType") data_type: String,   // 응답 자료 형식
        @Query("base_date") base_date: String,  // 발표 일자
        @Query("base_time") base_time: String,  // 발표 시각
        @Query("nx") nx: Int,                // 예보지점 X 좌표
        @Query("ny") ny: Int                 // 예보지점 Y 좌표
    ): Call<WEATHER>
}