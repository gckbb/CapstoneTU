package com.example.kakaotest.Utility.tmap

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.HashMap
import com.example.kakaotest.DataModel.metaRoute.*
import com.example.kakaotest.DataModel.tmap.*
import retrofit2.await
import retrofit2.awaitResponse


class ApiAdapter2 {
    fun apiRequest2(startX :Number, startY :Number, endX :Number, endY :Number) : MetaRoute?{
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://apis.openapi.sk.com/transit/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val apiService: ApiService2 = retrofit.create(ApiService2::class.java)

        var input = JsonModel(startX.toString(),startY.toString(),endX.toString(),endY.toString(),1,0,"json")


        val routeCall = apiService.getRoute(input)
        var totalTime : MetaRoute?
        val routeData = routeCall.execute()
        Log.d("ERROR",routeData.errorBody()?.string().toString())
        routeData.body()?.toString()?.let { Log.d("Plan", it) }
        totalTime = routeData.body()

        return totalTime
    }
}