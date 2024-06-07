package com.example.kakaotest.TourApi

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TourApiClient {
    companion object {
        private const val BASE_URL = "http://apis.data.go.kr/B551011/KorService1/"
        const val API_KEY = "Mv9rJ7cCS4dxLCBssOo64rQ+dPlbr10nAOl09/M8w0oZaEE9wgXksY61Tlk1+qUy40F2TVtSNxXvbEIaHGeRrg==" // TourAPI 인증 키

        fun create(): TourApiService {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val okHttpClient = OkHttpClient.Builder().build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson)) // Gson 변환기 설정
                .build()

            return retrofit.create(TourApiService::class.java)
        }
    }
}
