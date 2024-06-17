package com.example.kakaotest.TourApi

import android.annotation.SuppressLint
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

    private var isFirstLoad = true // 초기화 플래그

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

        tourapiSpinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.areaCode,
            android.R.layout.simple_spinner_item
        )

        tourapiSpinner_content.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.contentId,
            android.R.layout.simple_spinner_item
        )

        //뒤로가기 버튼
        binding.backBtn.setOnClickListener {
            finish()
        }

        tourapiSpinner_content.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (isFirstLoad) {
                        isFirstLoad = false // 초기화 이후에는 더 이상 실행하지 않음
                        return
                    }

                    // 첫 번째 스피너에서 선택된 항목에 따라 두 번째 스피너의 어댑터 업데이트
                    val selectedCategory = tourapiSpinner_content.selectedItem.toString()
                    Log.d("AreaCode", "selectedCategory: ${selectedCategory}")
                    val category1ArrayId = findContent(selectedCategory)
                    Log.d("AreaCode", "tourapiSpinner: ${category1ArrayId}")
                    tourapiSpinner1.adapter = ArrayAdapter.createFromResource(
                        this@TourApiActivity,
                        category1ArrayId,
                        android.R.layout.simple_spinner_item
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // 아무 것도 선택되지 않았을 때의 처리 (필요시 구현)
                }
            }


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
                Log.d("AreaCode", "selectedCategory1: ${selectedCategory1}")
                val selectedContent = tourapiSpinner_content.selectedItem.toString()
                Log.d("AreaCode", "selectedContent: ${selectedContent}")
                val category2ArrayId = findCategory(selectedCategory1, selectedContent)
                Log.d("AreaCode", "tourapiSpinner1: ${category2ArrayId}")
                if(category2ArrayId != 0)
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
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 두 번째 스피너에서 선택된 항목에 따라 세 번째 스피너의 어댑터 업데이트
                val selectedCategory2 = tourapiSpinner2.selectedItem.toString()
                val selectedContent = tourapiSpinner_content.selectedItem.toString()
                Log.d("AreaCode", "selectedContent: ${selectedContent}")
                val category3ArrayId = findCategory(selectedCategory2, selectedContent)
                Log.d("AreaCode", "tourapiSpinner2: ${category3ArrayId}")
                if(category3ArrayId != 0)
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


        binding.tourapiSearch.setOnClickListener {
            val selectedValuePosition = tourapiSpinner3.selectedItemPosition
            val selectedValue = categoryValues[selectedValuePosition]
            val selectedArea = resources.getStringArray(R.array.areaCode_values)[tourapiSpinner.selectedItemPosition]
            val selectedContentId = resources.getStringArray(R.array.contentId_values)[tourapiSpinner_content.selectedItemPosition]
            Log.d("AreaCode","selectedValue: ${selectedValue}" +
                    "selectedArea:  ${selectedArea}" +
            "selectedContentId: ${selectedContentId}")
            searchRecommendInArea(selectedValue, selectedArea, selectedContentId)
        }
        binding.areaBased.setOnClickListener {
            editor.clear().apply()
            Toast.makeText(this, "초기화 성공", Toast.LENGTH_SHORT).show()
            //Log.d("add_test","초기화 성공")
        }
    }

    private fun searchRecommendInArea(cat3: String, area: String, contentId: String) {
        scope.launch {
            try {
                Log.d("AreaCode", "${cat3}, ${area}, ${contentId}")
                val recommends = tourApiManager.searchRecommendInArea(cat3, area, contentId)
                Log.d("AreaCode", "searchRecommend")
                // RecyclerView 설정
                val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this@TourApiActivity)
                val adapter = RecommendAdapter(recommends.response.body.items.item)
                recyclerView.adapter = adapter
            } catch (e: Exception) {
                // 오류 처리
                Log.e("AreaCode", "음식점 검색 오류: $e")
            }
        }
    }

    private fun findCategory(cat: String, id: String): Int {
        var cate = 0 // 초기값 0으로 설정
        Log.d("AreaCode","id: ${id}")
        Log.d("AreaCode","cat: ${cat}")
        // id에 따른 카테고리 설정
        when (id) {
            "관광지" -> {
                cate = when (cat) {
                    "자연" -> R.array.category_A01
                    "자연관광지" -> R.array.category_A0101
                    "관광자원" -> R.array.category_A0102

                    "인문(문화/예술/역사)" -> R.array.category_A02_1
                    "역사관광지" -> R.array.category_A0201
                    "휴양관광지" -> R.array.category_A0202
                    "체험관광지" -> R.array.category_A0203
                    "산업관광지" -> R.array.category_A0204
                    "건축/조형물" -> R.array.category_A0205
                    else -> 0 // 이외의 경우에는 초기값인 0 반환
                }
            }
            "문화시설" -> {
                cate = when (cat) {
                    "인문(문화/예술/역사)" -> R.array.category_A02_2
                    "문화시설" -> R.array.category_A0206
                    else -> 0
                }
            }
            "축제공연행사" -> {
                cate = when (cat) {
                    "인문(문화/예술/역사)" -> R.array.category_A02_3
                    "축제" -> R.array.category_A0207
                    "공연/행사" -> R.array.category_A0208
                    else -> 0
                }
            }
            "레포츠" -> {
                cate = when (cat) {
                    "레포츠" -> R.array.category_A03
                    "레포츠소개" -> R.array.category_A0301
                    "육상 레포츠" -> R.array.category_A0302
                    "수상 레포츠" -> R.array.category_A0303
                    "항공 레포츠" -> R.array.category_A0304
                    "복합 레포츠" -> R.array.category_A0305
                    else -> 0
                }

            }

            "쇼핑" -> {
                cate = when (cat) {
                    "쇼핑" -> R.array.category_A04
                    "쇼핑점" -> R.array.category_A0401
                    else -> 0
                }
            }

            "음식" -> {
                cate = when (cat) {
                    "음식" -> R.array.category_A05
                    "음식점" -> R.array.category_A0502
                    else -> 0
                }
            }

            else -> 0
        }

        Log.d("AreaCode", "cate: $cate")
        return cate
    }


    private fun findContent(cat: String): Int {
        return when (cat) {
            "관광지" -> R.array.category1_1 //12
            "문화시설" -> R.array.category1_2 //14
            "축제공연행사" -> R.array.category1_3 //15
            "레포츠" -> R.array.category1_4 //28
            "쇼핑" -> R.array.category1_5 //38
            "음식" -> R.array.category1_6 //39
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
            "문화시설" -> resources.getStringArray(R.array.category_A0206_values)
            "축제" -> resources.getStringArray(R.array.category_A0207_values)
            "공연/행사" -> resources.getStringArray(R.array.category_A0208_values)

            "레포츠소개 " -> resources.getStringArray(R.array.category_A0301_values)
            "육상 레포츠" -> resources.getStringArray(R.array.category_A0302_values)
            "수상 레포츠" -> resources.getStringArray(R.array.category_A0303_values)
            "항공 레포츠" -> resources.getStringArray(R.array.category_A0304_values)
            "복합 레포츠" -> resources.getStringArray(R.array.category_A0305_values)

            "쇼핑점" -> resources.getStringArray(R.array.category_A0401_values)

            "음식점" -> resources.getStringArray(R.array.category_A0502_values)
            else -> arrayOf()
        }
    }

}