package com.example.kakaotest.TourApi

import android.util.Log
import com.example.kakaotest.DataModel.CategoryResponse
import com.example.kakaotest.DataModel.RestaurantResponse


class TourApiManager {

    suspend fun searchRestaurantsInArea(
        cat3 : String
    ): RestaurantResponse {
        val apiService = TourApiClient.create()
        Log.d("restest", "apiservice 실행")
        val response = apiService.searchRestaurants(
            apiKey = TourApiClient.API_KEY, // 여기를 변경
            mobileOS = "AND",
            mobileApp = "AppTest",
            arrange = "A",
            cat3 = cat3,
            type = "json",
            numOfRows = 30
        )
        Log.d("restest", "searchRestaurants 실행")
        return response
    }

    suspend fun searchCategory(cat1: String,cat2: String,cat3: String): CategoryResponse {
        val apiService = TourApiClient.create()
        Log.d("Restaurant", "category 실행")
        val response = apiService.searchCategory(
            apiKey = TourApiClient.API_KEY, // 여기를 변경
            mobileOS = "AND",
            mobileApp = "AppTest",
            cat1 = cat1,
            cat2 = cat2,
            cat3 = cat3,
            type = "json",
        )
        Log.d("Restaurant", "category 실행완료")
        return response
    }

    suspend fun searchCategory2(cat1: String,cat2: String): CategoryResponse {
        val apiService = TourApiClient.create()
        Log.d("Restaurant", "category 실행")
        val response = apiService.searchCategory2(
            apiKey = TourApiClient.API_KEY, // 여기를 변경
            mobileOS = "AND",
            mobileApp = "AppTest",
            cat1 = cat1,
            cat2 = cat2,
            type = "json"
        )
        Log.d("Restaurant", "category 실행완료")
        return response
    }

    suspend fun searchCategory3(cat1: String): CategoryResponse {
        val apiService = TourApiClient.create()
        Log.d("Restaurant", "category 실행")
        val response = apiService.searchCategory3(
            apiKey = TourApiClient.API_KEY, // 여기를 변경
            mobileOS = "AND",
            mobileApp = "AppTest",
            cat1 = cat1,
            type = "json"
        )
        Log.d("Restaurant", "category 실행완료")
        return response
    }

    suspend fun searchCategory4(): CategoryResponse {
        val apiService = TourApiClient.create()
        Log.d("Restaurant", "category 실행")
        val response = apiService.searchCategory4(
            apiKey = TourApiClient.API_KEY, // 여기를 변경
            mobileOS = "AND",
            mobileApp = "AppTest",
            type = "json"
        )
        Log.d("Restaurant", "category 실행완료")
        return response
    }
}