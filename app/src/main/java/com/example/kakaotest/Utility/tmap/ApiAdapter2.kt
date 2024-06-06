package com.example.kakaotest.Utility.tmap

import android.util.Log
import com.example.kakaotest.DataModel.metaRoute.JsonModel
import com.example.kakaotest.DataModel.metaRoute.MetaRoute
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class ApiAdapter2 {
    fun apiRequest2(startX :Number,startY :Number,endX :Number,endY :Number) : MetaRoute?{
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://apis.openapi.sk.com/transit/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val apiService: ApiService2 = retrofit.create(ApiService2::class.java)

        var input = JsonModel(startX.toString(),startY.toString(),endX.toString(),endY.toString(),1,0,"json")


        val routeCall = apiService.getRoute(input)
        var totalTime : MetaRoute?
        Log.d("ERROR",routeCall.execute().errorBody()?.string().toString())
        totalTime = routeCall.execute().body()

        return totalTime
    }
}