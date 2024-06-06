package com.example.kakaotest.Map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.tmap.SearchData
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.Utility.tmap.MakeRoute
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.example.kakaotest.R
import com.skt.tmap.TMapPoint
import kotlinx.coroutines.launch
import java.util.ArrayList
import java.util.LinkedList

class RouteListActivity : AppCompatActivity() {

    private val routetest = MakeRoute()
    var convertedFoodDataList : ArrayList<SelectedPlaceData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_route_list)
        val receivedDataList = intent.getParcelableArrayListExtra<SelectedPlaceData>("selectedPlaceDataList")
        val travelPlan = intent.getParcelableExtra<TravelPlan>("travelPlan")
        val receivedFoodDataList = intent.getParcelableArrayListExtra<SearchData>("selectedFoodDataList")



        Log.d("RouteListActivity",receivedDataList.toString()) //arraylistof<SelectedPlaceData>
        Log.d("RouteListActivity",receivedFoodDataList.toString()) //arraylistof<SearchData>




        if (receivedFoodDataList != null) {
            convertedFoodDataList = convertList(receivedFoodDataList)
        }
        val rentStartPoint:SelectedPlaceData
        val startDate = travelPlan!!.startDate?.day ?: 0
        val endDate = travelPlan.endDate?.day ?: 0
        val dateRange = endDate - startDate
        val activityTime = travelPlan.activityTime
        var restaurant = travelPlan.restaurant
        if(restaurant == null) {
            restaurant = "YES"
            Log.e("PLAN", "restaurant null error")
        }



        val path_1: Button = findViewById<Button>(R.id.path_1)
        val path_2: Button = findViewById<Button>(R.id.path_2)
        val nextButton: Button = findViewById<Button>(R.id.nextbutton)

        val backBtn = findViewById<ImageButton>(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()

        }

        // 클릭 이벤트 설정
        path_1.setOnClickListener {
            val intent = Intent(this, FirstRoute::class.java)
            val firstPlaceList =DayRoute(0)
            intent.putExtra("firstList", firstPlaceList)
            startActivity(intent)

        }

        path_2.setOnClickListener {
            val secondPlaceList =DayRoute(1)
            val intent = Intent(this, SecondRoute::class.java)

            intent.putExtra("secondList",  secondPlaceList)
            startActivity(intent)
        }
        nextButton.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)

            for(i in 0 until dateRange+1) {
                intent.putExtra("List${i+1}",routetest.printTotalRoute().getOrNull(i)?.dayRoute)
            }

            intent.putParcelableArrayListExtra("selectedPlaceDataList", ArrayList(receivedDataList))
            intent.putExtra("travelPlan", travelPlan)
            Log.d("PLAN",travelPlan.toString())
            startActivity(intent)
        }



        Log.d("PLAN","receivedDataList : \n"+ receivedDataList.toString())

        lifecycleScope.launch {
            try {
                // 비동기적으로 routeSet을 호출합니다.
                if(travelPlan.transportion == "자차" || travelPlan.transportion == "택시") {
                    routetest.routeSet(receivedDataList!!, receivedDataList!![0],0)
                    Log.d("PLAN", "Route type 0 Set")
                    routetest.routeStart(dateRange, activityTime!!, 1, convertedFoodDataList!!,restaurant,0)
                    Log.d("PLAN", "Route type 0 Started")
                }
                else if(travelPlan.transportion == "렌트") {
                    //렌트 선택 추가되면 테스트
                    //routetest.routeSet(receivedDataList!!, rentStartPoint,0)
                }
                else if(travelPlan.transportion == "버스") {
                    //할당량문제로 테스트 부족함
                    //routetest.routeSet(receivedDataList!!, receivedDataList!![0],1)
                }
                else if(travelPlan.transportion == "도보") { //도보 의미가없음...
                    routetest.routeSet(receivedDataList!!, receivedDataList!![0],2)
                    Log.d("PLAN", "Route type 2 Set ")
                    routetest.routeStart(dateRange, activityTime!!, 1, convertedFoodDataList!!,restaurant,2)
                    Log.d("PLAN", "Route type 2 Started")
                }
                routetest.printTotalRoute()
                Log.d("PLAN", "Total Route Printed")
                updateListView()
            } catch (e: Exception) {
                Log.e("CreatedRoute1", "Error: ${e.message}", e)
            }
        }


    }

    //날짜별 선택된 장소 리스트 반환
    private fun DayRoute(day : Int): LinkedList<SearchRouteData>? {
        val DayData = routetest.printTotalRoute().getOrNull(day)
        var DayPlaceList = LinkedList<SearchRouteData>()
        if (DayData != null) {
            // 첫 번째 날짜의 선택된 장소 리스트 가져오기
            DayPlaceList = DayData!!.dayRoute!!
            if ( DayPlaceList == null ||  DayPlaceList.isEmpty()) {
                Log.e("PLAN", "selectedPlaceList is null or empty")
            }
        }else {
            Log.e("PLAN", "No data available for the ${day+1}day")
        }
        Log.d("PLAN","${day+1}일차 선택장소 : $DayPlaceList")
        return  DayPlaceList
    }


    //날짜별 총 이동시간 (초->시간 변환)  반환
    private fun totalTime(day: Int): Double { //time은 n일차의 총 이동시간  (eg. firstDayData.totalTime 첫 번째 날짜의 총 이동 시간)
        val DayData = routetest.printTotalRoute().getOrNull(day)
        val totalTime =  DayData!!.totalTime.toDouble() / 3600
        val totalHour : Double = String.format("%.1f", totalTime).toDouble()
        Log.d("PLAN","${day+1}일차 총 이동예상 시간 : ${totalHour.toString()}")
        return totalHour
    }



    private fun updateListView() {
        val listView1 = findViewById<ListView>(R.id.listView1)
        val listView2 = findViewById<ListView>(R.id.listView2)
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val totalTime_1 = findViewById<TextView>(R.id.totalTime_1)
        val totalTime_2 = findViewById<TextView>(R.id.totalTime_2)


        val firstDayPlace = DayRoute(0)!!.map { it.pointdata?.placeName?: "Unknown Place"}
        val adapter1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, firstDayPlace)
        listView1.adapter = adapter1
        listView1.setOnTouchListener { _, _ ->
            scrollView.requestDisallowInterceptTouchEvent(true)
            false
        }


        totalTime_1.text = totalTime(0).toString() +"시간"



        val SecondDayPlace = DayRoute(1)!!.map { it.pointdata?.placeName ?: "Unknown Place"}
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_list_item_1, SecondDayPlace)
        listView2.adapter = adapter2
        listView2.setOnTouchListener { _, _ ->
            scrollView.requestDisallowInterceptTouchEvent(true)
            false
        }


        totalTime_2.text = totalTime(1).toString() +"시간"



    }


    //SearchData -> SelectedPlaceData
    private fun convertList(searchDataList: ArrayList<SearchData>): ArrayList<SelectedPlaceData> {
        return searchDataList.map { searchData ->
            SelectedPlaceData(
                placeName = searchData.id,
                tpoint = TMapPoint(searchData.tpoint.latitude,searchData.tpoint.longitude),
                address = searchData.address
            )

        } as ArrayList<SelectedPlaceData>
    }
}