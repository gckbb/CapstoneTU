package com.example.kakaotest.TourApi

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
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
    private lateinit var categoryValues: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = ActivityTourapiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MySavedRestaurants", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val tourapiSpinner1 = binding.tourapiSpinner1
        val tourapiSpinner2 = binding.tourapiSpinner2
        val tourapiSpinner3 = binding.tourapiSpinner3

//        val latitude = 37.5665
//        val longitude = 126.9780


        tourapiSpinner1.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.category1,
            android.R.layout.simple_spinner_item
        )

// 첫 번째 스피너의 선택 이벤트 리스너 설정
        tourapiSpinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 첫 번째 스피너에서 선택된 항목에 따라 두 번째 스피너의 어댑터 업데이트
                val selectedCategory1 = tourapiSpinner1.selectedItem.toString()
                val category2ArrayId = findCategory(selectedCategory1)
                tourapiSpinner2.adapter = ArrayAdapter.createFromResource(
                    this@TourApiActivity,
                    category2ArrayId,
                    android.R.layout.simple_spinner_item
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무 것도 선택되지 않았을 때의 처리 (필요시 구현)
            }
        }

// 두 번째 스피너의 선택 이벤트 리스너 설정
        tourapiSpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 두 번째 스피너에서 선택된 항목에 따라 세 번째 스피너의 어댑터 업데이트
                val selectedCategory2 = tourapiSpinner2.selectedItem.toString()
                val category3ArrayId = findCategory(selectedCategory2)
                tourapiSpinner3.adapter = ArrayAdapter.createFromResource(
                    this@TourApiActivity,
                    category3ArrayId,
                    android.R.layout.simple_spinner_item
                )

                categoryValues = getCategoryValues(selectedCategory2)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무 것도 선택되지 않았을 때의 처리 (필요시 구현)
            }
        }

// 세 번째 스피너의 선택 이벤트 리스너 설정 (필요시 구현)
//        tourapiSpinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // 아무 것도 선택되지 않았을 때의 처리 (필요시 구현)
//            }
//        }


        binding.tourapiSearch.setOnClickListener {
            Log.d("Restaurant","선택된 카테고리: ${tourapiSpinner3.selectedItem}")
            val selectedPosition = tourapiSpinner3.selectedItemPosition
            val selectedValue = categoryValues[selectedPosition]
            searchRestaurantsInArea(selectedValue)
        }
        binding.areaBased.setOnClickListener {
            editor.clear().apply()
            Toast.makeText(this, "초기화 성공", Toast.LENGTH_SHORT).show()
            //Log.d("add_test","초기화 성공")
        }
    }

    private fun searchRestaurantsInArea(cat3: String) {
        scope.launch {
            try {
                val restaurants = tourApiManager.searchRestaurantsInArea(cat3)
                // 결과 처리
                val items = restaurants.response.body.items
                for (restaurant in items.item) {
                    Log.d(
                        "Restaurant",
                        "음식점 이름: ${restaurant.title}, 주소: ${restaurant.addr1}, 이미지: ${restaurant.firstimage2}"
                    )
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

    private fun findCategory(cat: String): Int {
        return when (cat) {
            "자연" -> R.array.category_A01
            "인문(문화/예술/역사)" -> R.array.category_A02
            "레포츠" -> 1 // 레포츠 배열을 정의해야 함
            "쇼핑" -> 1 // 쇼핑 배열을 정의해야 함
            "음식" -> 1 // 음식 배열을 정의해야 함
            "숙박" -> 1 // 숙박 배열을 정의해야 함
            "추천코스" -> 0 // 추천코스 배열을 정의해야 함

            "자연관광지" -> R.array.category_A0101
            "관광자원" -> R.array.category_A0102

            "역사관광지" -> R.array.category_A0201
            "휴양관광지" -> R.array.category_A0202
            "체험관광지" -> R.array.category_A0203
            "산업관광지" -> R.array.category_A0204
            "건축/조형물" -> R.array.category_A0205
            "문화시설" -> 1 // 문화시설 배열을 정의해야 함
            "축제" -> 1 // 축제 배열을 정의해야 함
            "공연/행사" -> 1 // 공연/행사 배열을 정의해야 함
            else -> 1 // 기본 배열 리소스 (필요시)
        }
    }


    private fun getCategoryValues(cat: String): Array<String> {
        return when (cat) {
            "자연관광지" -> resources.getStringArray(R.array.category_A0101_values)
            "관광자원" -> resources.getStringArray(R.array.category_A0102_values)

            "역사관광지" -> resources.getStringArray(R.array.category_A0201_values)
            "휴양관광지" -> resources.getStringArray(R.array.category_A0202_values)
            "체험관광지" -> resources.getStringArray(R.array.category_A0203_values)
            "산업관광지" -> resources.getStringArray(R.array.category_A0204_values)
            "건축/조형물" -> resources.getStringArray(R.array.category_A0205_values)
            // 다른 하위 카테고리들의 값 배열 추가
            else -> arrayOf()
        }
    }

}