package com.example.kakaotest.Utility.tmap

import com.example.kakaotest.DataModel.tmap.FeatureCollection
import com.example.kakaotest.Utility.Url
import com.example.kakaotest.response.address.AddressInfoResponse
import com.example.kakaotest.response.search.SearchResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.HashMap

interface ApiService {

    @FormUrlEncoded
    @POST("routes")
    @Headers("appKey: 8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP")
    fun getRoute(
        @FieldMap param: HashMap<String, Any>
    ): Call<FeatureCollection>

    @GET(Url.GET_TMAP_LOCATION)
    suspend fun getSearchLocation(
        @Header("appKey") appKey: String = "8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP",
        @Query("version") version: Int = 1,
        @Query("callback") callback: String? = null,
        @Query("count") count: Int = 200,
        @Query("searchKeyword") keyword: String,
        @Query("areaLLCode") areaLLCode: String? = null,
        @Query("areaLMCode") areaLMCode: String? = null,
        @Query("resCoordType") resCoordType: String? = null,
        @Query("searchType") searchType: String? = null,
        @Query("multiPoint") multiPoint: String? = null,
        @Query("searchtypCd") searchtypCd: String? = null,
        @Query("radius") radius: String? = null,
        @Query("reqCoordType") reqCoordType: String? = null,
        @Query("centerLon") centerLon: String? = null,
        @Query("centerLat") centerLat: String? = null
    ): Response<SearchResponse>

    @GET(Url.GET_TMAP_REVERSE_GEO_CODE)
    suspend fun getReverseGeoCode(
        @Header("appKey") appKey: String = "8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP",
        @Query("version") version: Int = 1,
        @Query("callback") callback: String? = null,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("coordType") coordType: String? = null,
        @Query("addressType") addressType: String? = null
    ): Response<AddressInfoResponse>


}