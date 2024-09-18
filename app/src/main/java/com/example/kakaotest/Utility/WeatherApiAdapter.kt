package com.example.kakaotest.Utility.tmap

import android.util.Log
import com.example.kakaotest.DataModel.Weather.WeatherJsonModel
import com.example.kakaotest.DataModel.Weather.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.HashMap
import com.example.kakaotest.DataModel.metaRoute.*
import com.example.kakaotest.DataModel.tmap.*
import retrofit2.await
import retrofit2.awaitResponse
import com.example.kakaotest.Utility.CoordinateConverter
import com.example.kakaotest.Utility.CoordinatesXy
import com.google.gson.GsonBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class WeatherApiAdapter {
    fun apiRequest(lat : Double, lon : Double) : RESPONSE?{
        var gson = GsonBuilder().setLenient().create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val currentdate : LocalDate = LocalDate.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val formattedDate = currentdate.format(formatter)
        val apiService: WeatherApiService = retrofit.create(WeatherApiService::class.java)
        val converter : CoordinateConverter = CoordinateConverter()
        val converted : CoordinatesXy
        converted = converter.convertToXy(lat,lon)



        val routeCall = apiService.getRoute(1000,1,"JSON","20240918","0500","55","127")
        Log.d("weather",converted.nx.toString())
        Log.d("weather",converted.ny.toString())
        Log.d("weather",formattedDate)
        var totalTime : WEATHER
        val routeData = routeCall.execute()
        Log.d("ERROR",routeData.errorBody()?.string().toString())
        //routeData.body()?.toString()?.let { Log.d("Plan", it) }
        totalTime = routeData.body()!!

        return null
    }
}