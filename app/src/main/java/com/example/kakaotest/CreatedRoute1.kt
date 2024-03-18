package com.example.kakaotest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kakaotest.Plan.PMakeRoute
import com.example.kakaotest.Plan.RouteListAdapter
import com.example.kakaotest.Plan.SelectedPlaceData
import com.skt.tmap.TMapPoint
import kotlinx.coroutines.launch
import java.util.ArrayList

class CreatedRoute1 : AppCompatActivity() {

    private val routetest = PMakeRoute()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_route)



        // Intent로 전달된 데이터 가져오기
        val receivedDataList =
            intent.getParcelableArrayListExtra<SelectedPlaceData>("selectedPlaceDataList")

        // 로그에 받은 데이터 출력
        Log.d("receivedDataList", receivedDataList.toString())

        // 첫 번째 장소 설정
        val startPlace = receivedDataList?.getOrNull(0)
        Log.d("PLAN", startPlace.toString())

        // 루틴 설정 및 실행
        lifecycleScope.launch {
            routetest.routeSet(receivedDataList!!, receivedDataList[0])
            Log.d("SelectedPlace", "Route Set")
            routetest.routeStart(2, 6,1)
            Log.d("SelectedPlace", "Route Started")
            routetest.printTotalRoute()
            Log.d("SelectedPlace", "Total Route Printed")
            updateListView()
        }


        val path_1: Button = findViewById<Button>(R.id.path_1)
      /*  path_1.setOnClickListener {
            val firstDayRoute = routetest.printTotalRoute().firstOrNull()
            val formattedFirstDayRoute = firstDayRoute?.flatMap { it }?.map { it.toString() }?.let { ArrayList(it) } ?: ArrayList()
            val intent = Intent (this, FirstRoute::class.java)
            intent.putStringArrayListExtra("FirstDayRoute", formattedFirstDayRoute)
            startActivity(intent)
        }*/

        // 다음 액티비티로 이동하는 버튼 설정
        val nextButton: Button = findViewById<Button>(R.id.nextbutton)
        nextButton.setOnClickListener {
            val intent = Intent(this, PlanInfoActivity::class.java)
            startActivity(intent)
        }
    }

    // 리스트뷰 업데이트 함수
    // 리스트뷰 업데이트 함수
    // 리스트뷰 업데이트 함수
    private fun updateListView() {
        val listView = findViewById<ListView>(R.id.listView1)
        val routeListAdapter = RouteListAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        val listView1 = findViewById<ListView>(R.id.listView1)
        val routeListAdapter1 = RouteListAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        val firstDayRoute = routetest.printTotalRoute().firstOrNull() // 첫 번째 일자의 경로 가져오기

        if (firstDayRoute != null) {
            // 첫 번째 일자의 경로에서 각 장소를 각각 어댑터에 추가
            for (routeData in firstDayRoute) {
                routeListAdapter1.add(routeData.toString())
            }

        }
        listView1.adapter = routeListAdapter1

        val listView2 = findViewById<ListView>(R.id.listView2)
        val routeListAdapter2 = RouteListAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        val secondDayRoute = routetest.printTotalRoute().getOrNull(1)

        // 어댑터에 경로 리스트 설정
        routeListAdapter.addAll(routetest.printTotalRoute())
        if (secondDayRoute != null) {
            for (routeData in secondDayRoute){
                routeListAdapter2.add(routeData.toString())
            }
        }

        listView.adapter = routeListAdapter
        listView2.adapter = routeListAdapter2
    }








}