package com.example.kakaotest.TourApi

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
    var tourapiSpinner: Spinner= binding.tourapiSpinnerArea

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityTourapiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MySavedRestaurants", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val tourapiSpinner = binding.tourapiSpinnerArea
        val tourapiSpinner_content = binding.tourapiSpinnerContentId
        val tourapiSpinner1 = binding.tourapiSpinner1
        val tourapiSpinner2 = binding.tourapiSpinner2
        val tourapiSpinner3 = binding.tourapiSpinner3

        searchAreaCode()

        // 카테고리와 하위 카테고리를 직접 정의합니다.
        val categories = listOf(
            Category("자연", listOf(
                Subcategory("자연관광지", listOf("A01010100", "A01010200", "A01010300")),
                Subcategory("관광자원", listOf("A01020100", "A01020200"))
            )),
            Category("인문(문화/예술/역사)", listOf(
                Subcategory("역사관광지", listOf("A02010100", "A02010200")),
                Subcategory("휴양관광지", listOf("A02020100", "A02020200")),
                Subcategory("체험관광지", listOf("A02030100", "A02030200")),
                Subcategory("산업관광지", listOf("A02040100", "A02040200")),
                Subcategory("건축/조형물", listOf("A02050100", "A02050200")),
                Subcategory("문화시설", listOf("A02060100", "A02060200")),
                Subcategory("축제", listOf("A02070100", "A02070200")),
                Subcategory("공연/행사", listOf("A02080100", "A02080200"))
            ))
            // 다른 카테고리와 하위 카테고리도 비슷하게 추가합니다.
        )

        // 첫 번째 스피너에 카테고리 이름을 설정합니다.
        val categoryNames = categories.map { it.name }
        tourapiSpinner1.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)

        // 첫 번째 스피너 선택 리스너 설정
        tourapiSpinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                val subcategoryNames = selectedCategory.subcategories.map { it.name }
                tourapiSpinner2.adapter = ArrayAdapter(this@TourApiActivity, android.R.layout.simple_spinner_item, subcategoryNames)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // 두 번째 스피너 선택 리스너 설정
        tourapiSpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[tourapiSpinner1.selectedItemPosition]
                val selectedSubcategory = selectedCategory.subcategories[position]
                val subcategoryValues = selectedSubcategory.values
                tourapiSpinner3.adapter = ArrayAdapter(this@TourApiActivity, android.R.layout.simple_spinner_item, subcategoryValues)
                categoryValues = subcategoryValues.toTypedArray()  // 나중에 사용할 값을 저장합니다.
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // 검색 버튼 클릭 리스너 설정
        binding.tourapiSearch.setOnClickListener {
            val selectedValuePosition = tourapiSpinner3.selectedItemPosition
            val selectedValue = categoryValues[selectedValuePosition]
            val selectedArea = tourapiSpinner.selectedItem.toString()
            val selectedContentId = tourapiSpinner_content.selectedItem.toString()
            searchRecommendInArea(selectedValue, selectedArea, selectedContentId)
        }

        // 초기화 버튼 클릭 리스너 설정
        binding.areaBased.setOnClickListener {
            editor.clear().apply()
            Toast.makeText(this, "초기화 성공", Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchRecommendInArea(cat3: String, area: String, contentId: String) {
        scope.launch {
            try {
                val recommends = tourApiManager.searchRecommendInArea(cat3, area, contentId)
                // RecyclerView 설정
                val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this@TourApiActivity)
                val adapter = RecommendAdapter(recommends.response.body.items.item)
                recyclerView.adapter = adapter
            } catch (e: Exception) {
                // 오류 처리
                Log.e("TourApiActivity", "음식점 검색 오류: $e")
            }
        }
    }

    private fun searchAreaCode() {
        scope.launch {
            try {
                val area = tourApiManager.searchAreaCode()
                if (area.response.header.resultCode == "0000") {
                    val areaNames = area.response.body.items.item.map { it.name }
                    updateSpinner(areaNames)
                } else {
                    Log.d("Areacode", "API 호출 실패: ${area.response.header.resultMsg}")
                }
            } catch (e: Exception) {
                Log.d("Areacode", "API 호출 중 오류 발생: ${e.message}")
            }
        }
    }

    private fun updateSpinner(areaNames: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, areaNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tourapiSpinner.adapter = adapter
    }

    data class Category(val name: String, val subcategories: List<Subcategory>)
    data class Subcategory(val name: String, val values: List<String>)
}
