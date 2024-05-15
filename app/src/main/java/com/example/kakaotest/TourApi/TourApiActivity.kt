package com.example.kakaotest.TourApi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.R
import com.example.kakaotest.databinding.ActivityTourapiBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TourApiActivity : AppCompatActivity() {
    private val tourApiManager = TourApiManager()
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = ActivityTourapiBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val latitude = 37.5665
        val longitude = 126.9780

        binding.restaurant.setOnClickListener {
            searchRestaurantsInArea(latitude, longitude)
        }
        binding.areaBased.setOnClickListener {
            searchAreaBasedList(latitude, longitude)
        }
    }

    private fun searchRestaurantsInArea(latitude: Double, longitude: Double) {
        scope.launch {
            try {
                val restaurants = tourApiManager.searchRestaurantsInArea(latitude,longitude)
                // 결과 처리
                val items = restaurants.response.body.items
                for (restaurant in items.item) {
                    Log.d("Restaurant", "음식점 이름: ${restaurant.title}, 주소: ${restaurant.addr1}, 이미지: ${restaurant.firstimage2}")
                }
                // RecyclerView 설정
                val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this@TourApiActivity)
                val adapter = RestaurantAdapter(restaurants.response.body.items.item)
                recyclerView.adapter = adapter
            } catch (e: Exception) {
                // 오류 처리
                Log.e("TourApiActivity", "음식점 검색 오류: $e")
            }
        }
    }

    private fun searchAreaBasedList(latitude: Double, longitude: Double) {
        scope.launch {
            try {
                val areaData = tourApiManager.searchAreaBasedList(latitude, longitude)
                // 결과 처리
                val items = areaData.response.body.items
                for (place in items.item) {
                    Log.d("Place", "장소 이름: ${place.title}, 주소: ${place.addr1}")
                }
                // RecyclerView 설정
                val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this@TourApiActivity)
                val adapter = AreaBasedAdapter(areaData.response.body.items.item)
                recyclerView.adapter = adapter
            } catch (e: Exception) {
                // 오류 처리
                Log.e("TourApiActivity", "지역 기반 리스트 검색 오류: $e")
            }
        }
    }
}
