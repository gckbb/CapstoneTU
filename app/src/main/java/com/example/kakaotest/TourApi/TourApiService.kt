package com.example.kakaotest.TourApi

import com.example.kakaotest.DataModel.CategoryResponse
import com.example.kakaotest.DataModel.RestaurantResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TourApiService {
    @GET("areaBasedList1")
    suspend fun searchRestaurants(
        @Query("serviceKey") apiKey: String,
        @Query("MobileOS") mobileOS: String,
        @Query("MobileApp") mobileApp: String,
        @Query("arrange") arrange: String,
        @Query("cat3") cat3: String,
        @Query("_type") type: String,
        @Query("numOfRows") numOfRows: Int
    ): RestaurantResponse
    @GET("categoryCode1")
    suspend fun searchCategory(
        @Query("serviceKey") apiKey: String,
        @Query("MobileOS") mobileOS: String,
        @Query("MobileApp") mobileApp: String,
        @Query("_type") type: String,
        @Query("cat1") cat1: String,
        @Query("cat2") cat2: String,
        @Query("cat3") cat3: String
    ): CategoryResponse

    @GET("categoryCode1")
    suspend fun searchCategory2(
        @Query("serviceKey") apiKey: String,
        @Query("MobileOS") mobileOS: String,
        @Query("MobileApp") mobileApp: String,
        @Query("_type") type: String,
        @Query("cat1") cat1: String,
        @Query("cat2") cat2: String,
    ): CategoryResponse

    @GET("categoryCode1")
    suspend fun searchCategory3(
        @Query("serviceKey") apiKey: String,
        @Query("MobileOS") mobileOS: String,
        @Query("MobileApp") mobileApp: String,
        @Query("_type") type: String,
        @Query("cat1") cat1: String,
    ): CategoryResponse

    @GET("categoryCode1")
    suspend fun searchCategory4(
        @Query("serviceKey") apiKey: String,
        @Query("MobileOS") mobileOS: String,
        @Query("MobileApp") mobileApp: String,
        @Query("_type") type: String,
    ): CategoryResponse
}