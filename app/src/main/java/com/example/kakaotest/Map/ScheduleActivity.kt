package com.example.kakaotest.Map
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.kakaotest.CheckList.CheckListActivity
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.example.kakaotest.HomeActivity

import com.example.kakaotest.R
import com.example.kakaotest.Utility.TravelPlanManager
import com.example.kakaotest.databinding.ActivityScheduleBinding

class ScheduleActivity : AppCompatActivity() {
    private val travelPlanManager = TravelPlanManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstList = intent.getParcelableArrayListExtra<SearchRouteData>("firstList") //경로
        val secondList = intent.getParcelableArrayListExtra<SearchRouteData>("secondList") //경로

        val receivedDataList = intent.getParcelableArrayListExtra<SelectedPlaceData>("selectedPlaceDataList")



        val travelPlan = intent.getParcelableExtra<TravelPlan>("travelPlan")
        Log.d("PLAN",travelPlan.toString())
        val placename = findViewById<TextView>(R.id.placename)
        val firstdate = findViewById<TextView>(R.id.date1)
        val lastdate = findViewById<TextView>(R.id.date2)
        val who = findViewById<TextView>(R.id.who)
        val day1 = findViewById<Button>(R.id.day1)
        var day2 = findViewById<Button>(R.id.day2)
        var day3 = findViewById<Button>(R.id.day3)
        var day4 = findViewById<Button>(R.id.day4)
        var day5 = findViewById<Button>(R.id.day5)
        var day6 = findViewById<Button>(R.id.day6)
        var day7 = findViewById<Button>(R.id.day7)
        var day8 = findViewById<Button>(R.id.day8)



        travelPlan?.let { plan ->
            placename.text = plan.where?.placeName ?: ""
            firstdate.text = plan.startDate?.date ?: ""
            lastdate.text = plan.endDate?.date ?: ""
            who.text = plan.who ?: ""
       //     day1.text = plan.startDate?.day.toString() ?: ""
          //  day2.text = (plan.startDate?.day?.plus(1)).toString()
        }
        val startDate = travelPlan!!.startDate?.day ?: 0
        val endDate = travelPlan.endDate?.day ?: 0
        val dateRange = endDate - startDate

        for (i in 0..dateRange) {
            val textView = when (i) {
                0 -> day1
                1 -> day2
                2 -> day3
                3 -> day4
                4 -> day5
                5 -> day6
                6 -> day7
                7 -> day8

                else -> null // 예외 처리
            }
            textView?.text = (startDate + i).toString()
        }
        travelPlanManager.updatePlan(destination = firstList)
        travelPlanManager.updatePlan(destination = secondList)


        Log.d("travelPlan","travelPlan update : "+travelPlan)


        var firstListTime = firstList?.map { it.time }
        Log.d("PLAN",firstListTime.toString())


        val time1_1=findViewById<TextView>(R.id.time1_1)
        val time1_2=findViewById<TextView>(R.id.time1_2)
        val time1_3=findViewById<TextView>(R.id.time1_3)
        val time1_4=findViewById<TextView>(R.id.time1_4)
        val time1_5=findViewById<TextView>(R.id.time1_5)
        val time1_6=findViewById<TextView>(R.id.time1_6)
        val time1_7=findViewById<TextView>(R.id.time1_7)
        val time1_8=findViewById<TextView>(R.id.time1_8)
        val time1_9=findViewById<TextView>(R.id.time1_9)
        val time1_10=findViewById<TextView>(R.id.time1_10)


        val firstdayTime = mutableListOf<TextView>(
            time1_1, time1_2, time1_3, time1_4,time1_5, time1_6,
            time1_7,time1_8, time1_9,time1_10)

        val time1_0_1 = findViewById<ImageView>(R.id.line1_0_1)
        val time1_1_2 = findViewById<ImageView>(R.id.line1_1_2)
        val time1_2_3 = findViewById<ImageView>(R.id.line1_2_3)
        val time1_3_4 = findViewById<ImageView>(R.id.line1_3_4)
        val time1_4_5 = findViewById<ImageView>(R.id.line1_4_5)
        val time1_5_6 = findViewById<ImageView>(R.id.line1_5_6)
        val time1_6_7 = findViewById<ImageView>(R.id.line1_6_7)
          val time1_7_8 = findViewById<ImageView>(R.id.line1_7_8)
        val time1_8_9 = findViewById<ImageView>(R.id.line1_8_9)
        val time1_9_10 = findViewById<ImageView>(R.id.line1_9_10)
        val firsttimeLine = mutableListOf<ImageView>(time1_0_1,time1_1_2, time1_2_3, time1_3_4, time1_4_5, time1_5_6, time1_6_7, time1_7_8, time1_8_9, time1_9_10)


        val adjustedFirstListTime = firstListTime?.drop(1) // 첫 번째 요소를 건너뛴 새로운 리스트
        // 이동시간이 없는 경우 나머지 숨기기
        for (i in firstListTime!!.size until firstdayTime.size) {

            firstdayTime[i].visibility = View.GONE
            firsttimeLine[i].visibility=View.GONE
        }


        adjustedFirstListTime?.forEachIndexed { index, time ->
            val (hours, minutes) = convertSecondsToTime(time.toDouble())
            firstdayTime[index].apply {
                visibility = View.VISIBLE
                text = String.format("%02d:%02d", hours, minutes) // 시간 설정
            }
            // 라인 표시
            if (index < firsttimeLine.size) {
                firsttimeLine[index].visibility = View.VISIBLE
                firsttimeLine[index+1].visibility=View.VISIBLE
            }
        }


        var secondListTime = secondList?.map { it.time }

        val time2_1=findViewById<TextView>(R.id.time2_1)
        val time2_2=findViewById<TextView>(R.id.time2_2)
        val time2_3=findViewById<TextView>(R.id.time2_3)
        val time2_4=findViewById<TextView>(R.id.time2_4)
        val time2_5=findViewById<TextView>(R.id.time2_5)
        val time2_6=findViewById<TextView>(R.id.time2_6)
        val time2_7=findViewById<TextView>(R.id.time2_7)
        val seconddayTime = mutableListOf<TextView>(time2_1, time2_2, time2_3, time2_4, time2_5, time2_6, time2_7)


        val time2_0_1 = findViewById<ImageView>(R.id.line2_0_1)
        val time2_1_2 = findViewById<ImageView>(R.id.line2_1_2)
        val time2_2_3 = findViewById<ImageView>(R.id.line2_2_3)
        val time2_3_4 = findViewById<ImageView>(R.id.line2_3_4)
        val time2_4_5 = findViewById<ImageView>(R.id.line2_4_5)
        val time2_5_6 = findViewById<ImageView>(R.id.line2_5_6)
        val time2_6_7 = findViewById<ImageView>(R.id.line2_6_7)
        //  val time7_8 = findViewById<ImageView>(R.id.line6_7)
        val secondtimeLine = mutableListOf<ImageView>(time2_0_1,time2_1_2, time2_2_3, time2_3_4, time2_4_5, time2_5_6, time2_6_7)


        val adjustedSecondListTime = secondListTime?.drop(1) // 첫 번째 요소를 건너뛴 새로운 리스트
        // 이동시간이 없는 경우 나머지 숨기기
        for (i in secondListTime!!.size until seconddayTime.size) {

            seconddayTime[i].visibility = View.GONE
            secondtimeLine[i].visibility=View.GONE
        }


        adjustedSecondListTime?.forEachIndexed { index, time ->
            val (hours, minutes) = convertSecondsToTime(time.toDouble())
            seconddayTime[index].apply {
                visibility = View.VISIBLE
                text = String.format("%02d:%02d", hours, minutes) // 시간 설정
            }
            // 라인 표시
            if (index < secondtimeLine.size) {
                secondtimeLine[index].visibility = View.VISIBLE
                secondtimeLine[index+1].visibility=View.VISIBLE
            }
        }


        binding.path1.setOnClickListener {
            val intent = Intent(this, FirstRoute::class.java)
            intent.putExtra("firstList",  firstList)
            startActivity(intent)
        }

        binding.path2.setOnClickListener {
            val intent = Intent(this, SecondRoute::class.java)
            intent.putExtra("secondList",  secondList)
            startActivity(intent)
        }

        // 리스트뷰를 찾아냅니다.
        val placeListView1 = findViewById<ListView>(R.id.placeListView1)
        // 어댑터 생성 및 설정
        val firstListView = firstList?.map { "${it.pointdata?.placeName}" } ?: emptyList()

        // 어댑터 생성
        val firstAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1,  firstListView)

        // ListView에 어댑터 설정
        placeListView1.adapter = firstAdapter

        binding.placeListContainer1.visibility = View.VISIBLE



        val placeListView2 = findViewById<ListView>(R.id.placeListView2)
        // 어댑터 생성 및 설정
        val secondListView = secondList?.map { "${it.pointdata?.placeName}" } ?: emptyList()

        // 어댑터 생성
        val secondAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1,  secondListView)

        // ListView에 어댑터 설정
        binding.placeListView2.adapter = secondAdapter



        val placeListContainerList = mutableListOf<LinearLayout>(
            binding.placeListContainer1,binding.placeListContainer2,binding.placeListContainer3,binding.placeListContainer4,
            binding.placeListContainer5,binding.placeListContainer6,binding.placeListContainer7,binding.placeListContainer8)

        dayListShow(binding.day1, placeListContainerList, 0)
        dayListShow(binding.day2, placeListContainerList, 1)
        dayListShow(binding.day3, placeListContainerList, 2)
        dayListShow(binding.day4, placeListContainerList, 3)
        dayListShow(binding.day5, placeListContainerList, 4)
        dayListShow(binding.day6, placeListContainerList, 5)
        dayListShow(binding.day7, placeListContainerList, 6)
        dayListShow(binding.day8, placeListContainerList, 7)



        // firstList가 null이 아닌지 확인
        if (firstList != null) {
            singleRoute(time1_1, 0, firstList)
            singleRoute(time1_2, 1, firstList)
            singleRoute(time1_3, 2, firstList)
            singleRoute(time1_4, 3, firstList)
            singleRoute(time1_5, 4, firstList)
            singleRoute(time1_6, 5, firstList)
            singleRoute(time1_7, 6, firstList)
        } else {
            // firstList가 null일 때의 처리
            Log.e("MainActivity", "firstList is null")
        }

        //singleRouteShow(firstList)

        binding.backBtn.setOnClickListener {
            finish()
        }



        // 홈 버튼 클릭 리스너 설정
        binding.home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }

        binding.checkList.setOnClickListener {
            val intent = Intent(this, CheckListActivity ::class.java)
            startActivity(intent)
        }


    }

    fun convertSecondsToTime(seconds: Double?): Pair<Int?, Int?> {
        val hours = seconds?.div(3600)?.toInt()
        val minutes = (seconds?.rem(3600))?.div(60)?.toInt()
        return Pair(hours, minutes)
    }

/*
    fun singleRouteShow(daylist:ArrayList<SearchRouteData>){
        // firstList가 null이 아닌지 확인
        if (daylist != null) {
            singleRoute(time1_1, 0, daylist)
            singleRoute(time1_2, 1, daylist)
            singleRoute(time1_3, 2, firstList)
            singleRoute(time1_4, 3, firstList)
            singleRoute(time1_5, 4, firstList)
            singleRoute(time1_6, 5, firstList)
            singleRoute(time1_7, 6, firstList)
        } else {
            // firstList가 null일 때의 처리
            Log.e("MainActivity", "$daylist is null")
        }

    }*/


    fun singleRoute(time:TextView,value : Int,daylist:ArrayList<SearchRouteData>){
        time.setOnClickListener {
       //     time.setBackgroundColor(R.color.button)
            time.background = ContextCompat.getDrawable(this, R.color.button)
            val intent = Intent(this, SingleRoute::class.java)
            intent.putExtra("time",value)
            intent.putExtra("dayList", daylist)
            startActivity(intent)
        }

    }

    fun dayListShow(day: Button, placeListContainerList: List<LinearLayout>, i: Int) {
        day.setOnClickListener {
            for (index in placeListContainerList.indices) {
                if (index == i) {
                    placeListContainerList[index].visibility = View.VISIBLE
                } else {
                    placeListContainerList[index].visibility = View.GONE
                }
            }
        }
    }

}
