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
import com.example.kakaotest.CashBook.CashBookActivity
import com.example.kakaotest.CheckList.CheckListActivity
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.example.kakaotest.HomeActivity

import com.example.kakaotest.R
import com.example.kakaotest.Utility.SharedPreferenceUtil
import com.example.kakaotest.Utility.TravelPlanManager
import com.example.kakaotest.databinding.ActivityScheduleBinding
import com.google.api.Distribution.BucketOptions.Linear

class ScheduleActivity : AppCompatActivity() {
    private val travelPlanManager = TravelPlanManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstList = intent.getParcelableArrayListExtra<SearchRouteData>("firstList") //경로
    //    val secondList = intent.getParcelableArrayListExtra<SearchRouteData>("secondList") //경로

        val dayRouteList = mutableListOf<ArrayList<SearchRouteData>?>()
        val travelPlan : TravelPlan? = SharedPreferenceUtil.getTravelPlanFromSharedPreferences(this)
        val startDate = travelPlan!!.startDate?.day ?: 0
        val endDate = travelPlan.endDate?.day ?: 0
        val dateRange = endDate - startDate


        for(i in 0 until dateRange+1) {
            val routeData: ArrayList<SearchRouteData>? =
                intent.getParcelableArrayListExtra("List${i + 1}")
            dayRouteList.add(routeData ?: ArrayList())
        }

        val receivedDataList : ArrayList<SelectedPlaceData>? = SharedPreferenceUtil.getRouteFromSharedPreferences(this)


   //     val travelPlan = intent.getParcelableExtra<TravelPlan>("travelPlan")
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
     //   travelPlanManager.updatePlan(destination = firstList)
    //    travelPlanManager.updatePlan(destination = secondList)


        Log.d("travelPlan","travelPlan update : "+travelPlan)

    /*    binding.path1.setOnClickListener {
            val intent = Intent(this, FirstRoute::class.java)
            intent.putExtra("firstList",  firstList)
            startActivity(intent)
        }



        binding.path1.setOnClickListener {
            val intent = Intent(this, SecondRoute::class.java)
            intent.putExtra("secondList",  secondList)
            startActivity(intent)
        }*/

        // 리스트뷰를 찾아냅니다.
        val placeListView1 = findViewById<ListView>(R.id.placeListView1)


        updateListView(placeListView1,dayRouteList[0]!!)
        time(dayRouteList[0]!!)

        binding.day1.setOnClickListener {
            updateListView(placeListView1, dayRouteList[0]!!)
            time(dayRouteList[0]!!)

        }
        binding.day2.setOnClickListener {
            updateListView(placeListView1, dayRouteList[1]!!)
            time(dayRouteList[1]!!)
        }



        binding.cash.setOnClickListener {
            val intent = Intent(this, CashBookActivity::class.java)
            startActivity(intent)
        }


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




        // firstList가 null이 아닌지 확인
        if (firstList != null) {
            singleRoute(time1_1, 0, firstList)
            singleRoute(time1_2, 1, firstList)
            singleRoute(time1_3, 2, firstList)
            singleRoute(time1_4, 3,firstList)
            singleRoute(time1_5, 4, firstList)
            singleRoute(time1_6, 5, firstList)
            singleRoute(time1_7, 6, firstList)
        } else {
            // firstList가 null일 때의 처리
            Log.e("ScheduleActivity", "firstList is null")
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



    fun time(daylist: ArrayList<SearchRouteData>){
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


        val dayTime = mutableListOf<TextView>(
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
        val timeLine = mutableListOf<ImageView>(time1_0_1,time1_1_2, time1_2_3, time1_3_4, time1_4_5, time1_5_6, time1_6_7, time1_7_8, time1_8_9, time1_9_10)

        dayTime.forEach { it.visibility = View.GONE }
        timeLine.forEach { it.visibility = View.GONE }

        var ListTime = daylist?.map{it.time}
        val adjustedListTime = ListTime?.drop(1)


        // 이동시간이 없는 경우 나머지 숨기기
        for (i in ListTime!!.size until ListTime.size) {

            dayTime[i].visibility = View.GONE
            timeLine[i].visibility=View.GONE
        }


        adjustedListTime?.forEachIndexed { index, time ->
            val (hours, minutes) = convertSecondsToTime(time.toDouble())
            dayTime[index].apply {
                visibility = View.VISIBLE
                text = String.format("%02d:%02d", hours, minutes) // 시간 설정
            }
            // 라인 표시
            if (index < timeLine.size) {
                timeLine[index].visibility = View.VISIBLE
                timeLine[index+1].visibility=View.VISIBLE
            }
        }

    }

    fun singleRoute(time:TextView,value : Int,daylist:ArrayList<SearchRouteData>){
        time.setOnClickListener {

            val intent = Intent(this, SingleRoute::class.java)
            intent.putExtra("time",value)
            intent.putExtra("dayList", daylist)
            startActivity(intent)
        }

    }

    fun allRoute(path:Button,data : ArrayList<SearchRouteData>){
        path.setOnClickListener {
            val intent = Intent(this,FirstRoute::class.java)

            intent.putExtra("dayList",data)
            startActivity(intent)
        }
    }


    private fun updateListView(listView:ListView,data : ArrayList<SearchRouteData>){



        val dateList = data?.map { "${it.pointdata?.placeName}" } ?: emptyList()

        // 어댑터 생성
        val dateAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1,  dateList)

        // ListView에 어댑터 설정
        listView.adapter = dateAdapter

        val listcontainer = findViewById<LinearLayout>(R.id.placeListContainer1)
        listcontainer.visibility = View.VISIBLE

        val pathBtn = findViewById<Button>(R.id.path_1)
        allRoute(pathBtn,data)

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