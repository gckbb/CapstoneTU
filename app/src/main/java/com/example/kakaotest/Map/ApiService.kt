package com.example.kakaotest.Map

import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.HashMap

interface ApiService {

    @FormUrlEncoded
    @POST("routes?version=1&callback=function")
    @Headers("appKey: 8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP")
    fun getRoute(
        @FieldMap param: HashMap<String, Any>
    ): Call<FeatureCollection>

}