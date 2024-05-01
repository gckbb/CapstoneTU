package com.example.kakaotest.TourApi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.R
import com.example.kakaotest.TourApi.TourApiManager.Companion.searchRestaurantsInArea
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class TourApiActivity: AppCompatActivity() {
    private val scope = MainScope()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tourapi)




        // 서울시청을 중심으로 음식점 검색
        val latitude = 37.5665
        val longitude = 126.9780

        scope.launch {
            val restaurants =
                searchRestaurantsInArea(latitude, longitude)

            // 검색 결과를 로그로 출력
            val items = restaurants.response.body.items
            for (restaurant in items.item) {
                Log.d("Restaurant", "음식점 이름: ${restaurant.title}, 주소: ${restaurant.addr1}")
            }
            val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this@TourApiActivity)
            val adapter = RestaurantAdapter(restaurants.response.body.items.item)
            recyclerView.adapter = adapter

        }



    }
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel() // Activity가 소멸될 때 코루틴을 취소하여 메모리 누수를 방지합니다.
    }
}