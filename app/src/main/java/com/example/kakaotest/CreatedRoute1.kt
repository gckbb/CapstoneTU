package com.example.kakaotest

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
import com.example.kakaotest.Plan.PMakeRoute
import com.example.kakaotest.Plan.RouteListAdapter
import com.example.kakaotest.Plan.SelectedPlaceData
import com.skt.tmap.TMapPoint
import kotlinx.coroutines.launch
import java.util.ArrayList

class CreatedRoute1 : AppCompatActivity() {

    private val routetest = PMakeRoute()
    private val TotalRoute = arrayListOf<SelectedPlaceData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_route)
        val routeData = routetest.printTotalRoute()
        /*

        val path_1: Button = findViewById<Button>(R.id.path_1)
        path_1.setOnClickListener {
            // Intent 생성
            val intent = Intent(this, FirstRoute::class.java)
            intent.putExtra("firstDay", firstDay)

            startActivity(intent)
        }

        val path_2: Button = findViewById<Button>(R.id.path_2)
        path_2.setOnClickListener {
            // Intent 생성
            val intent = Intent(this, SecondRoute::class.java)

            intent.putExtra("secondDay", secondDay)
            startActivity(intent)
        }
*/
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
            routetest.routeStart(2, 6, 1)
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


    private fun updateListView() {
        val listView1 = findViewById<ListView>(R.id.listView1)
        val listView2 = findViewById<ListView>(R.id.listView2)
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val totalTime_1 = findViewById<TextView>(R.id.totalTime_1)
        val totalTime_2 = findViewById<TextView>(R.id.totalTime_2)
        val routeData = routetest.printTotalRoute()
        val routeStringList = routeData.routeStringList
        val routeTpointList = routeData.routeTpointList

        val firstPlaceNames = routeStringList.getOrNull(0) ?: emptyList()
        // 리스트뷰 어댑터 생성 및 설정
        val adapter1 =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, firstPlaceNames)
        listView1.adapter = adapter1
        listView1.setOnTouchListener { v, event ->
            scrollView.requestDisallowInterceptTouchEvent(true)
            false
        }

        val secondPlaceNames = routeStringList.getOrNull(1)?: emptyList()
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, secondPlaceNames)
        listView2.adapter = adapter2
        listView2.setOnTouchListener { v, event ->
            scrollView.requestDisallowInterceptTouchEvent(true)
            false
        }
    }
}
