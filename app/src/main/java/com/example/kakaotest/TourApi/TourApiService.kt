package com.example.kakaotest.TourApi

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
    @GET("categoryCode1")
    suspend fun searchCategory(
        @Query("serviceKey") apiKey: String,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") mobileOS: String,
        @Query("MobileApp") mobileApp: String,
        @Query("_type") type: String,
        @Query("listYN") listYN: String,
        @Query("contentTypeId") contentTypeId: Int,
        @Query("modifiedtime") modifiedtime: String,
        @Query("cat1") cat1: String,
        @Query("cat2") cat2: String,
        @Query("cat3") cat3: String,
    ): RestaurantResponse
}