package com.example.kakaotest.Utility.tmap

import com.example.kakaotest.DataModel.tmap.FeatureCollection
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.HashMap

interface ApiService {

    @FormUrlEncoded
    @POST("routes?version=1&callback=function")
    @Headers("appKey: UEpEdFrLsN3grYYQwxoTIanD6Zt1CNpT9xgFiznr")
    fun getRoute(
        @FieldMap param: HashMap<String, Any>
    ): Call<FeatureCollection>

}