package com.example.kakaotest.Map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kakaotest.Utility.tmap.MakeRoute
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.example.kakaotest.R
import kotlinx.coroutines.launch

class RouteListActivity : AppCompatActivity() {

    private val routetest = MakeRoute()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_route)
        val receivedDataList = intent.getParcelableArrayListExtra<SelectedPlaceData>("selectedPlaceDataList")

        val path_1: Button = findViewById<Button>(R.id.path_1)
        val path_2: Button = findViewById<Button>(R.id.path_2)
        val nextButton: Button = findViewById<Button>(R.id.nextbutton)

        // 클릭 이벤트 설정
        path_1.setOnClickListener {
            val intent = Intent(this, FirstRoute::class.java)
            // TotalRouteData에서 첫 번째 날짜의 데이터 가져오기
            val firstDayData = routetest.printTotalRoute().firstOrNull()
            Log.d("PLAN","firstDayData : \n"+firstDayData)
            // 첫 번째 날짜의 데이터가 있을 때 처리
            if (firstDayData != null) {
                // 첫 번째 날짜의 선택된 장소 리스트 가져오기
                val firstList = firstDayData.dayRoute
                intent.putExtra("firstList",  firstList)

                startActivity(intent)
            } else {
                Log.e("PLAN", "No data available for the first day")
            }
        }

        path_2.setOnClickListener {
            val intent = Intent(this, SecondRoute::class.java)
            val secondDayData = routetest.printTotalRoute().getOrNull(1)
            Log.d("PLAN","secondDayData : \n"+secondDayData)
            if (secondDayData != null) {
                // 두 번째 날짜의 선택된 장소 리스트 가져오기
                val secondList = secondDayData.dayRoute

                intent.putExtra("secondList",  secondList)

                startActivity(intent)
            } else {
                Log.e("CreatedRoute1", "No data available for the secondday")
            }
        }

        nextButton.setOnClickListener {
            val intent = Intent(this, PlanInfoInput::class.java)
            startActivity(intent)
        }


        Log.d("PLAN","receivedDataList : \n"+ receivedDataList.toString())

        lifecycleScope.launch {
            try {
                // 비동기적으로 routeSet을 호출합니다.
                routetest.routeSet(receivedDataList!!, receivedDataList[0])
                Log.d("PLAN", "Route Set")
                // 비동기적으로 routeStart를 호출합니다.
                routetest.routeStart(2, 6)
                Log.d("PLAN", "Route Started")
                routetest.printTotalRoute()
                Log.d("PLAN", "Total Route Printed")
                updateListView()
            } catch (e: Exception) {
                Log.e("CreatedRoute1", "Error: ${e.message}", e)
            }
        }
    }

    private fun updateListView() {
        val listView1 = findViewById<ListView>(R.id.listView1)
        val listView2 = findViewById<ListView>(R.id.listView2)
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val totalTime_1 = findViewById<TextView>(R.id.totalTime_1)
        val totalTime_2 = findViewById<TextView>(R.id.totalTime_2)
        val totalRouteData = routetest.printTotalRoute()
        Log.d("PLAN","updateListView() totalRouteData : \n" + totalRouteData)


        // 첫 번째 날짜의 모든 데이터
        val firstDayData = routetest.printTotalRoute().firstOrNull()
        if (firstDayData != null) {
            Log.d("PLAN","updateListView() firstDayData : \n" + firstDayData)

            //  Log.d("PLAN","${i}일째 총 이동시간 : "+hour.toString())


            val totalTime = firstDayData.totalTime.toDouble() / 3600 // 첫 번째 날짜의 총 이동 시간
            val totalHour: Double = String.format("%.1f", totalTime).toDouble()
            val selectedPlaceList = firstDayData.dayRoute // 첫 번째 날짜의 선택된 장소 데이터 목록
            totalTime_1.text = totalHour.toString() +"시간"
            val firstDayPlace = selectedPlaceList!!.map { it.pointdata?.placeName }
            val adapter1 =
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, firstDayPlace)
            listView1.adapter = adapter1
            listView1.setOnTouchListener { _, _ ->
                scrollView.requestDisallowInterceptTouchEvent(true)
                false
            }
        }

        val secondDayData = totalRouteData.getOrNull(1)
        if (secondDayData!= null) {
            Log.d("PLAN","updateListView() secondDayData : \n" + secondDayData)
            val totalTime = secondDayData.totalTime.toDouble() / 3600 // 첫 번째 날짜의 총 이동 시간
            val totalHour: Double = String.format("%.1f", totalTime).toDouble() // 첫 번째 날짜의 총 이동 시간
            val selectedPlaceList = secondDayData.dayRoute // 첫 번째 날짜의 선택된 장소 데이터 목록
            totalTime_2.text=totalHour.toString() +"시간"
            val secondDayPlace = selectedPlaceList!!.map { it.pointdata?.placeName }
            val adapter2 =
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, secondDayPlace)
            listView2.adapter = adapter2
            listView2.setOnTouchListener { _, _ ->
                scrollView.requestDisallowInterceptTouchEvent(true)
                false
            }
        }



    }
}


