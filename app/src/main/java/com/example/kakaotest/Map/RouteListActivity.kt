package com.example.kakaotest.Map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.metaRoute.*
import com.example.kakaotest.DataModel.tmap.SearchData
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.Utility.tmap.MakeRoute
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.example.kakaotest.R
import com.example.kakaotest.Utility.SharedPreferenceUtil
import com.google.gson.Gson
import com.skt.tmap.TMapPoint
import kotlinx.coroutines.launch
import java.util.ArrayList
import java.util.LinkedList

class RouteListActivity : AppCompatActivity() {

    private val routetest = MakeRoute()
    var convertedFoodDataList : ArrayList<SelectedPlaceData>? = null
    private var dateRange =0
    private var startDate=0
    private var endDate =0
    var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_route_list)
        val receivedDataList : ArrayList<SelectedPlaceData>? = SharedPreferenceUtil.getDataFromSharedPreferences(this)
        val travelPlan : TravelPlan? = SharedPreferenceUtil.getTravelPlanFromSharedPreferences(this)
        val receivedFoodDataList: ArrayList<SearchData>? = SharedPreferenceUtil.getFoodFromSharedPreferences(this)




        if (receivedFoodDataList != null) {
            convertedFoodDataList = convertList(receivedFoodDataList)
            receivedDataList?.addAll(convertedFoodDataList!!)
        }
        val rentStartPoint:SelectedPlaceData
        startDate = travelPlan!!.startDate?.day ?: 0
        endDate = travelPlan.endDate?.day ?: 0
        dateRange = endDate - startDate
        val activityTime = travelPlan.activityTime
        var restaurant = travelPlan.restaurant
        if(restaurant == null) {
            restaurant = "yes"
            Log.e("PLAN", "restaurant null error")
        }


        for (i in 0..dateRange){
            buttonClick(i)

        }
        val day1 = findViewById<Button>(R.id.day1)
        var day2 = findViewById<Button>(R.id.day2)
        var day3 = findViewById<Button>(R.id.day3)
        var day4 = findViewById<Button>(R.id.day4)
        var day5 = findViewById<Button>(R.id.day5)
        for (i in 0..dateRange) {
            val textView = when (i) {
                0 -> day1
                1 -> day2
                2 -> day3
                3 -> day4
                4 -> day5
                else -> null // 예외 처리
            }
            textView?.text = (startDate + i).toString()
        }



        val nextButton: Button = findViewById<Button>(R.id.nextbutton)

        val backBtn = findViewById<ImageButton>(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()

        }




        nextButton.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)

            if(travelPlan.transportion == "버스") {
                for(i in 0 until dateRange+1) {
                    intent.putExtra("List${i+1}",gson.toJson(routetest.printTotalRoute2().getOrNull(i)))
                }
            }
            else {
                for(i in 0 until dateRange+1) {
                    intent.putExtra("List${i+1}",routetest.printTotalRoute().getOrNull(i)?.dayRoute)
                }
            }

            //       intent.putParcelableArrayListExtra("selectedPlaceDataList", ArrayList(receivedDataList))
            //     intent.putExtra("travelPlan", travelPlan)
            //   Log.d("PLAN",travelPlan.toString())
            startActivity(intent)
        }



        Log.d("PLAN","receivedDataList : \n"+ receivedDataList.toString())

        lifecycleScope.launch {
            try {
                val listView = findViewById<ListView>(R.id.listView1)
                listView.visibility = View.VISIBLE
                val emptyList = listOf("로딩 중...") // Adjust the type based on your data
                val adapter = ArrayAdapter(this@RouteListActivity, android.R.layout.simple_list_item_1, emptyList)
                listView.adapter = adapter

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
                    routetest.routeSet(receivedDataList!!, receivedDataList!![0],1)
                    routetest.routeStart2(dateRange, activityTime!!, 1, convertedFoodDataList!!,restaurant,1)
                }
                else if(travelPlan.transportion == "도보") { //도보 의미가없음...
                    routetest.routeSet(receivedDataList!!, receivedDataList!![0],2)
                    Log.d("PLAN", "Route type 2 Set ")
                    routetest.routeStart(dateRange, activityTime!!, 1, convertedFoodDataList!!,restaurant,2)
                    Log.d("PLAN", "Route type 2 Started")
                }
                routetest.printTotalRoute()
                Log.d("PLAN", "Total Route Printed")

                if(travelPlan.transportion == "버스") BusUpdateList(0,DayRoute2(0)!!.dayRoute)
                else CarUpdateList(0, CarRoute(0))


               // UpdateList(0,)
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

    private fun DayRoute2(day : Int): MetaDayRoute? {
        val DayData = routetest.printTotalRoute2().getOrNull(day)
        var DayPlaceList = LinkedList<SearchMetaData>()
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
        return DayData
    }

    private fun CarRoute(day : Int):  ArrayList<SearchRouteData> {
        val DayData = routetest.printTotalRoute()?.let {
            it.getOrNull(day)?.dayRoute
        }

        val arrayListData = ArrayList(DayData)
        var DayPlaceList = ArrayList<SearchRouteData>()

        Log.d("PLAN","${day+1}일차 선택장소 : $DayPlaceList")
        return  arrayListData
    }


    //날짜별 총 이동시간 (초->시간 변환)  반환
    private fun totalTime(day: Int): Double { //time은 n일차의 총 이동시간  (eg. firstDayData.totalTime 첫 번째 날짜의 총 이동 시간)
        val DayData = routetest.printTotalRoute().getOrNull(day)
        val totalTime =  DayData!!.totalTime.toDouble() / 3600
        val totalHour : Double = String.format("%.1f", totalTime).toDouble()
        Log.d("PLAN","${day+1}일차 총 이동예상 시간 : ${totalHour.toString()}")
        return totalHour
    }

    private fun totalTime2(day: Int): Double { //time은 n일차의 총 이동시간  (eg. firstDayData.totalTime 첫 번째 날짜의 총 이동 시간)
        val DayData = routetest.printTotalRoute2().getOrNull(day)
        val totalTime =  DayData!!.totalTime.toDouble() / 3600
        val totalHour : Double = String.format("%.1f", totalTime).toDouble()
        Log.d("PLAN","${day+1}일차 총 이동예상 시간 : ${totalHour.toString()}")
        return totalHour
    }


    private fun CarUpdateList(day:Int,data : ArrayList<SearchRouteData>){
        val travelPlan : TravelPlan? = SharedPreferenceUtil.getTravelPlanFromSharedPreferences(this)
        val listView = findViewById<ListView>(R.id.listView1)
        val totalTime = findViewById<TextView>(R.id.totalTime_1)





        val carRoute = data?.map { "${it.pointdata?.placeName}" } ?: emptyList()
        val adapter1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, carRoute)
        listView.adapter = adapter1
        totalTime.text = totalTime(day).toString() + "시간"

    }

    fun BusUpdateList(value:Int,data : LinkedList<SearchMetaData>){
        val listView = findViewById<ListView>(R.id.listView1)
        val totalTime = findViewById<TextView>(R.id.totalTime_1)

        val travelPlan : TravelPlan? = SharedPreferenceUtil.getTravelPlanFromSharedPreferences(this)
        val dateList = data?.map { "${it.pointdata?.placeName}" } ?: emptyList()

        // 어댑터 생성
        val dateAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1,  dateList)

        // ListView에 어댑터 설정
        listView.adapter = dateAdapter

        totalTime.text = totalTime2(value).toString() + "시간"


    }






    private fun updateListView2() {
        val listView1 = findViewById<ListView>(R.id.listView1)
        val listView2 = findViewById<ListView>(R.id.listView2)
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val totalTime_1 = findViewById<TextView>(R.id.totalTime_1)
        val totalTime_2 = findViewById<TextView>(R.id.totalTime_2)
        val travelPlan : TravelPlan? = SharedPreferenceUtil.getTravelPlanFromSharedPreferences(this)

        val firstDayPlace = DayRoute2(0)!!.dayRoute.map { it.pointdata?.placeName?: "Unknown Place"}
        val adapter1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, firstDayPlace)
        listView1.adapter = adapter1
        listView1.setOnTouchListener { _, _ ->
            scrollView.requestDisallowInterceptTouchEvent(true)
            false
        }


        totalTime_1.text = totalTime2(0).toString() +"시간"



        val SecondDayPlace = DayRoute2(1)!!.dayRoute.map { it.pointdata?.placeName ?: "Unknown Place"}
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_list_item_1, SecondDayPlace)
        listView2.adapter = adapter2
        listView2.setOnTouchListener { _, _ ->
            scrollView.requestDisallowInterceptTouchEvent(true)
            false
        }


        totalTime_2.text = totalTime2(1).toString() +"시간"



    }

    private fun buttonClick(value:Int){
        val day1 = findViewById<Button>(R.id.day1)
        var day2 = findViewById<Button>(R.id.day2)
        var day3 = findViewById<Button>(R.id.day3)
        var day4 = findViewById<Button>(R.id.day4)
        var day5 = findViewById<Button>(R.id.day5)

        val travelPlan : TravelPlan? = SharedPreferenceUtil.getTravelPlanFromSharedPreferences(this)
        val dayButtons = listOf(day1, day2, day3, day4, day5)

        for (i in 0..dateRange) {
            if (i < dayButtons.size) {
                dayButtons[i].text = (startDate + i).toString()
                dayButtons[i].setOnClickListener {
                    if(travelPlan!!.transportion == "버스") {
                        BusUpdateList(i, DayRoute2(i)!!.dayRoute)

                    }else{
                        CarUpdateList(i,CarRoute(i))

                    }
                }
            }
        }





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