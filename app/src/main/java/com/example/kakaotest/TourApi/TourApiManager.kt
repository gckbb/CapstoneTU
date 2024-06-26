package com.example.kakaotest.TourApi

import android.util.Log
import com.example.kakaotest.DataModel.AreaDataResponse
import com.example.kakaotest.DataModel.CategoryResponse
import com.example.kakaotest.DataModel.RecommendResponse


class TourApiManager {

    suspend fun searchRecommendInArea(
        cat3: String,
        area: String,
        contentId: String
    ): RecommendResponse {
        val apiService = TourApiClient.create()
        Log.d("AreCode", "apiservice 실행")
        val response = apiService.searchRecommend(
            apiKey = TourApiClient.API_KEY, // 여기를 변경
            mobileOS = "AND",
            mobileApp = "AppTest",
            arrange = "A",
            cat3 = cat3,
            areaCode = area,
            contentTypeId = contentId,
            type = "json",
            numOfRows = 30
        )
        Log.d("detailCode", "searchRestaurants 실행")
        Log.d("detailCode", "Response: ${response.toString()}") // 응답 데이터 로깅
        return response
    }

    suspend fun getDetailIntro(
        contentId: String,
        contentTypeId: String
    ): RecommendResponse {
        val apiService = TourApiClient.create()
        Log.d("detailCode", "apiservice 실행")
        val response = apiService.getdetailIntro(
            apiKey = TourApiClient.API_KEY,
            mobileOS = "AND",
            mobileApp = "AppTest",
            type = "json",
            contentId = contentId,
            contentTypeId = contentTypeId
        )
        Log.d("detailCode", "getDetail 실행")
        Log.d("detailCode", "Response: ${response}")
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

    suspend fun searchAreaCode(): AreaDataResponse {
        val apiService = TourApiClient.create()
        Log.d("AreaCode", "areadata 실행")
        val response = apiService.searchAreaCode(
            apiKey = TourApiClient.API_KEY, // 여기를 변경
            mobileOS = "AND",
            mobileApp = "AppTest",
            type = "json"
        )
        Log.d("AreaCode", "areadata 실행완료")
        return response
    }


}