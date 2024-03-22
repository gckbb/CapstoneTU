package com.example.kakaotest

import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kakaotest.Plan.PMakeRoute
import com.example.kakaotest.Plan.SelectedPlace
import com.example.kakaotest.Plan.SelectedPlaceData
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.Serializable

class CreatedRoute1 : AppCompatActivity() {

    private val routetest = PMakeRoute()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_route)

        val path_1: Button = findViewById<Button>(R.id.path_1)
        val path_2: Button = findViewById<Button>(R.id.path_2)
        val nextButton: Button = findViewById<Button>(R.id.nextbutton)

        // 클릭 이벤트 설정
        path_1.setOnClickListener {
            val intent = Intent(this, FirstRoute::class.java)
            // TotalRouteData에서 첫 번째 날짜의 데이터 가져오기
            val firstDayData = routetest.printTotalRoute().routeDataList.firstOrNull()

            // 첫 번째 날짜의 데이터가 있을 때 처리
            if (firstDayData != null) {
                // 첫 번째 날짜의 선택된 장소 리스트 가져오기
                val selectedPlaceList = firstDayData?.selectedPlaceList

                val selectedPlaceArrayList = ArrayList<SelectedPlace>(selectedPlaceList ?: emptyList())

                intent.putParcelableArrayListExtra("selectedPlaceList", selectedPlaceArrayList)


                // 액티비티 시작
                startActivity(intent)
            } else {
                Log.e("CreatedRoute1", "No data available for the first day")
            }
        }

        path_2.setOnClickListener {
            val intent = Intent(this, SecondRoute::class.java)
            startActivity(intent)
        }

        nextButton.setOnClickListener {
            val intent = Intent(this, PlanInfoActivity::class.java)
            startActivity(intent)
        }

        val receivedDataList = intent.getParcelableArrayListExtra<SelectedPlaceData>("selectedPlaceDataList")
        Log.d("receivedDataList", receivedDataList.toString())

        lifecycleScope.launch {
            routetest.routeSet(receivedDataList!!, receivedDataList[0])
            Log.d("SelectedPlace", "Route Set")
            routetest.routeStart(2, 6, 1)
            Log.d("SelectedPlace", "Route Started")
            routetest.printTotalRoute()
            Log.d("SelectedPlace", "Total Route Printed")
            updateListView()
        }
    }

    private fun updateListView() {
        val listView1 = findViewById<ListView>(R.id.listView1)
        val listView2 = findViewById<ListView>(R.id.listView2)
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val totalTime_1 = findViewById<TextView>(R.id.totalTime_1)
        val totalTime_2 = findViewById<TextView>(R.id.totalTime_2)
        val totalRouteData = routetest.printTotalRoute()



        // 첫 번째 날짜의 모든 데이터
        val firstDayData = totalRouteData.routeDataList.firstOrNull()
        if (firstDayData != null) {
            val date = firstDayData.date // 첫 번째 날짜
            val totalTime = firstDayData.totalTime // 첫 번째 날짜의 총 이동 시간
            val selectedPlaceList = firstDayData.selectedPlaceList // 첫 번째 날짜의 선택된 장소 데이터 목록
            totalTime_1.text = totalTime
            val firstDayPlace = selectedPlaceList.map { it.name }
            val adapter1 =
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, firstDayPlace)
            listView1.adapter = adapter1
            listView1.setOnTouchListener { _, _ ->
                scrollView.requestDisallowInterceptTouchEvent(true)
                false
            }
        }

        val secondDayData = totalRouteData.routeDataList.getOrNull(1)
        if (secondDayData!= null) {
            val date = secondDayData.date // 두 번째 날짜
            val totalTime = secondDayData.totalTime // 첫 번째 날짜의 총 이동 시간
            val selectedPlaceList = secondDayData.selectedPlaceList // 첫 번째 날짜의 선택된 장소 데이터 목록
            totalTime_2.text=totalTime
            val secondDayPlace = selectedPlaceList.map { it.name }
            val adapter2 =
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, secondDayPlace)
            listView2.adapter = adapter2
            listView2.setOnTouchListener { _, _ ->
                scrollView.requestDisallowInterceptTouchEvent(true)
                false
            }
        }

        listView1.setOnTouchListener { _, _ ->
            scrollView.requestDisallowInterceptTouchEvent(true)
            false
        }

    }
}


