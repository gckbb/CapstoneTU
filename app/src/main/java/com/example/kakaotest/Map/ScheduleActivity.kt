package com.example.kakaotest.Map
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData

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
            day1.text = plan.startDate?.day.toString() ?: ""
            day2.text = (plan.startDate?.day?.plus(1)).toString()
        }

        travelPlanManager.updatePlan(destination = firstList)
        travelPlanManager.updatePlan(destination = secondList)


        Log.d("travelPlan","travelPlan update : "+travelPlan)


        var firstListTime = firstList?.map { it.time }
        Log.d("PLAN",firstListTime.toString())


        val time1=findViewById<TextView>(R.id.time1)
        val time2=findViewById<TextView>(R.id.time2)
        val time3=findViewById<TextView>(R.id.time3)
        val time4=findViewById<TextView>(R.id.time4)
        val time5=findViewById<TextView>(R.id.time5)
        val time6=findViewById<TextView>(R.id.time6)
        val time7=findViewById<TextView>(R.id.time7)
        val firstdayTime = mutableListOf<TextView>(time1, time2, time3, time4, time5, time6, time7)


        val time0_1 = findViewById<ImageView>(R.id.line0_1)
        val time1_2 = findViewById<ImageView>(R.id.line1_2)
        val time2_3 = findViewById<ImageView>(R.id.line2_3)
        val time3_4 = findViewById<ImageView>(R.id.line3_4)
        val time4_5 = findViewById<ImageView>(R.id.line4_5)
        val time5_6 = findViewById<ImageView>(R.id.line5_6)
        val time6_7 = findViewById<ImageView>(R.id.line6_7)
        //  val time7_8 = findViewById<ImageView>(R.id.line6_7)
        val firsttimeLine = mutableListOf<ImageView>(time0_1,time1_2, time2_3, time3_4, time4_5, time5_6, time6_7)


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

        val time_1=findViewById<TextView>(R.id.time_1)
        val time_2=findViewById<TextView>(R.id.time_2)
        val time_3=findViewById<TextView>(R.id.time_3)
        val time_4=findViewById<TextView>(R.id.time_4)
        val time_5=findViewById<TextView>(R.id.time_5)
        val time_6=findViewById<TextView>(R.id.time_6)
        val time_7=findViewById<TextView>(R.id.time_7)
        val seconddayTime = mutableListOf<TextView>(time_1, time_2, time_3, time_4, time_5, time_6, time_7)


        val time_0_1 = findViewById<ImageView>(R.id.line_0_1)
        val time_1_2 = findViewById<ImageView>(R.id.line_1_2)
        val time_2_3 = findViewById<ImageView>(R.id.line_2_3)
        val time_3_4 = findViewById<ImageView>(R.id.line_3_4)
        val time_4_5 = findViewById<ImageView>(R.id.line_4_5)
        val time_5_6 = findViewById<ImageView>(R.id.line_5_6)
        val time_6_7 = findViewById<ImageView>(R.id.line_6_7)
        //  val time7_8 = findViewById<ImageView>(R.id.line6_7)
        val secondtimeLine = mutableListOf<ImageView>(time_0_1,time_1_2, time_2_3, time_3_4, time_4_5, time_5_6, time_6_7)


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


        binding.day1.setOnClickListener {
            binding.placeListContainer1.visibility = View.VISIBLE //listView1이 나타나져 있을 때 클릭하면 그대로 보이고
            binding.placeListContainer2.visibility = View.GONE //얘는 없어

        }
        binding.day2.setOnClickListener {
            binding.placeListContainer2.visibility = View.VISIBLE //listView1이 나타나져있을 때 day2를 클릭하면 listView2나오고 1은 사라져
            binding.placeListContainer1.visibility = View.GONE

        }





        /* 홈 버튼 클릭 리스너 설정
        binding.home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }*/


    }

    fun convertSecondsToTime(seconds: Double?): Pair<Int?, Int?> {
        val hours = seconds?.div(3600)?.toInt()
        val minutes = (seconds?.rem(3600))?.div(60)?.toInt()
        return Pair(hours, minutes)
    }

}
