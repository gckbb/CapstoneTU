package com.example.kakaotest.TourApi

import android.util.Log
import com.example.kakaotest.DataModel.AreaDataResponse
import com.example.kakaotest.DataModel.RestaurantResponse


class TourApiManager {

    suspend fun searchRestaurantsInArea(
        latitude: Double,
        longitude: Double
    ): RestaurantResponse {
        val apiService = TourApiClient.create()
        Log.d("restest", "apiservice 실행")
        val response = apiService.searchRestaurants(
            apiKey = TourApiClient.API_KEY, // 여기를 변경
            numOfRows = 20,
            pageNo = 5,
            mobileOS = "AND",
            mobileApp = "AppTest",
            arrange = "A",
            mapX = longitude,
            mapY = latitude,
            radius = 5000,
            type = "json",
            listYN = "Y",
            contentTypeId = 39,
            modifiedtime = ""
        )
        Log.d("restest", "searchRestaurants 실행")
        return response
    }

    suspend fun searchCategory(cat1: String, cat2: String, cat3: String): RestaurantResponse {
        val apiService = TourApiClient.create()
        Log.d("areatest", "AreaData_apiservice 실행")
        val response = apiService.searchCategory(
            apiKey = TourApiClient.API_KEY, // 여기를 변경
            numOfRows = 10,
            pageNo = 1,
            mobileOS = "AND",
            mobileApp = "AppTest",
            cat1 = "cat1",
            cat2 = "cat2",
            cat3 = "cat3",
            type = "json",
            listYN = "Y",
            contentTypeId = 39,
            modifiedtime = ""
        )
        Log.d("areatest", "searchAreaData 실행")
        return response
    }
}