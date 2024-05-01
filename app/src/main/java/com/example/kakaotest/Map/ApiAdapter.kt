package com.example.kakaotest.Map

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.HashMap

class ApiAdapter {
    fun apiRequest(startX :Number,startY :Number,endX :Number,endY :Number) : Number?{
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://apis.openapi.sk.com/tmap/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService: ApiService = retrofit.create(ApiService::class.java)

        var input = HashMap<String,Any>()
        input["startY"] = startY
        input["startX"] = startX
        input["endY"] = endY
        input["endX"] = endX
        input["totalValue"] = 2

        val routeCall = apiService.getRoute(input)
        var totalTime : Number?



        totalTime = routeCall.execute().body()?.features?.get(0)?.properties?.totalTime



        return totalTime
    }
}