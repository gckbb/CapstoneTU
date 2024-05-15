package com.example.kakaotest.TourApi

import com.example.kakaotest.DataModel.AreaDataResponse
import com.example.kakaotest.DataModel.RestaurantResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TourApiService {
    @GET("locationBasedList1")
    suspend fun searchRestaurants(
        @Query("serviceKey") apiKey: String,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") mobileOS: String,
        @Query("MobileApp") mobileApp: String,
        @Query("arrange") arrange: String,
        @Query("mapX") mapX: Double,
        @Query("mapY") mapY: Double,
        @Query("radius") radius: Int,
        @Query("_type") type: String,
        @Query("listYN") listYN: String,
        @Query("contentTypeId") contentTypeId: Int,
        @Query("modifiedtime") modifiedtime: String
    ): RestaurantResponse
    @GET("areaBasedList1")
    suspend fun searchAreaData(
        @Query("serviceKey") apiKey: String,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") mobileOS: String,
        @Query("MobileApp") mobileApp: String,
        @Query("arrange") arrange: String,
        @Query("mapX") mapX: Double,
        @Query("mapY") mapY: Double,
        @Query("radius") radius: Int,
        @Query("_type") type: String,
        @Query("listYN") listYN: String,
        @Query("contentTypeId") contentTypeId: Int,
        @Query("modifiedtime") modifiedtime: String
    ): AreaDataResponse
}


