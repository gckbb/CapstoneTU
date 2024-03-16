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
import kotlinx.coroutines.launch

class CreatedRoute : AppCompatActivity() {

    private val routetest = PMakeRoute()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_route)

        // 화면에서 버튼을 참조
        val firstDayButton: Button = findViewById(R.id.firstDay)
        val secondDayButton: Button = findViewById(R.id.secondDay)

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
            routetest.routeStart(2, 6)
            Log.d("SelectedPlace", "Route Started")
            routetest.printTotalRoute()
            Log.d("SelectedPlace", "Total Route Printed")
            updateListView()
        }

        // 다음 액티비티로 이동하는 버튼 설정
        val nextButton: Button = findViewById<Button>(R.id.nextbutton)
        nextButton.setOnClickListener {
            val intent = Intent(this, PlanInfoActivity::class.java)
            startActivity(intent)
        }
    }

    // 리스트뷰 업데이트 함수
    private fun updateListView() {
        val listView = findViewById<ListView>(R.id.firstlistView)
        val routeListAdapter = RouteListAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())

        // 어댑터에 경로 리스트 설정
        routeListAdapter.addAll(routetest.printTotalRoute())

        listView.adapter = routeListAdapter
    }
}
