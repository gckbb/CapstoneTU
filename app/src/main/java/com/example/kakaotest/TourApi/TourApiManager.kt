package com.example.kakaotest.TourApi

import android.util.Log
import com.example.kakaotest.DataModel.RestaurantResponse


class TourApiManager {
    companion object {
        suspend fun searchRestaurantsInArea(latitude: Double, longitude: Double): RestaurantResponse {
            val apiService = TourApiClient.create()
            Log.d("test","apiservice 실행")
            val response = apiService.searchRestaurants(
                apiKey = TourApiClient.API_KEY, // 여기를 변경
                numOfRows = 10,
                pageNo = 1,
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
            Log.d("test","searchRestaurants 실행")
            return response
            }
        }
    }

