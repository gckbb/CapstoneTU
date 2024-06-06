package com.example.kakaotest.Utility.tmap

import com.example.kakaotest.DataModel.metaRoute.*
import com.example.kakaotest.DataModel.tmap.FeatureCollection
import com.example.kakaotest.Utility.Url
import com.example.kakaotest.response.address.AddressInfoResponse
import com.example.kakaotest.response.search.SearchResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.HashMap

interface ApiService2 {


    @POST("routes")
    @Headers("appKey: UEpEdFrLsN3grYYQwxoTIanD6Zt1CNpT9xgFiznr",
        "accept: application/json",
        "content-type: application/json")
    fun getRoute(
        @Body jsonparams: JsonModel
    ): Call<MetaRoute>


}