package com.example.kakaotest.Map


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.kakaotest.DataModel.Date
import com.example.kakaotest.DataModel.Place
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.Fragment.DatePickerFragment
import com.example.kakaotest.R
import com.example.kakaotest.Utility.TravelPlanManager
import com.example.kakaotest.databinding.ActivityPlanInfoBinding
import java.util.Calendar

class PlanInfoInput : AppCompatActivity() {
    private var firstday: String = ""
    private var secondday : String = ""
    private var who: String ?=""
    private var transport : String = ""
    private var theme : String ?= ""
    private var activity : String ?= ""
    private var plan = TravelPlan()
    private val travelPlanManager = TravelPlanManager()
    private var mBinding: ActivityPlanInfoBinding? = null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityPlanInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)




        Log.d("Plan","PlaninfoInput Activity success")
        val selectedPlace = intent.getParcelableExtra<Place>("region")


        Log.d("PLAN", selectedPlace.toString())
        var selectedDate: Calendar = Calendar.getInstance()



        selectedPlace?.let { place ->
            val placeName = place.placeName
            val whereTextView = findViewById<TextView>(R.id.where)
            whereTextView.text = placeName
        }
        travelPlanManager.updatePlan(where=selectedPlace)
        val firstdate: TextView = findViewById<TextView>(R.id.day1txt)
        val seconddate: TextView = findViewById<TextView>(R.id.day2txt)

        Log.d("Plan","place : $selectedPlace")
        /*언제?*/
        // 첫 번째 날짜 선택 버튼 클릭 이벤트 처리
        var start_selectedYear: Int? = null
        var start_selectedMonth: Int? = null
        var start_selectedDay: Int? = null
        val date = ArrayList<Date>()
        val travelplan = ArrayList<TravelPlan>()

        binding.day1btn.setOnClickListener {
            val dialogFragment = DatePickerFragment { year, month, day ->
                start_selectedYear = year
                start_selectedMonth = month + 1
                start_selectedDay = day


                firstday = "$start_selectedYear/$start_selectedMonth/$start_selectedDay"
                firstdate.text = firstday // 첫 번째 날짜 TextView에 선택된 날짜 설정
                Toast.makeText(binding.root.context, firstday, Toast.LENGTH_SHORT).show()
                val day1 = Date(date= firstday,year = start_selectedYear, month=start_selectedMonth,day=start_selectedDay)
                date.add(day1)
                travelPlanManager.updatePlan(startDate = date[0])
                Log.d("date","첫째날 : "+date[0].toString())

            }
            dialogFragment.show(supportFragmentManager, "firstDatePicker")
        }



        //    val plan = TravelPlan(where = selectedPlace, startDate = firstday, endDate = secondday, who = who, transportion = transport, theme = theme, activity = activity, destinations = null)

        var last_selectedYear: Int? = null
        var last_selectedMonth: Int? = null
        var last_selectedDay: Int? = null


        binding.day2btn.setOnClickListener {
            val dialogFragment = DatePickerFragment { year, month, day ->
                last_selectedYear = year
                last_selectedMonth =month + 1
                last_selectedDay =day


                secondday = "$last_selectedYear/$last_selectedMonth/$last_selectedDay"
                seconddate.text = secondday // 첫 번째 날짜 TextView에 선택된 날짜 설정
                Toast.makeText(binding.root.context, firstday, Toast.LENGTH_SHORT).show()
                val day2 = Date(date =secondday ,year = last_selectedYear, month=last_selectedMonth,day=last_selectedDay)
                date.add(day2)

                travelPlanManager.updatePlan(endDate = date[1])
                Log.d("date","마지막 날 : "+date[1].toString())

            }
            dialogFragment.show(supportFragmentManager, "firstDatePicker")
        }



        val friendBtn: Button = findViewById(R.id.friend)
        val familyBtn = findViewById<Button>(R.id.family)
        val coupleBtn= findViewById<Button>(R.id.couple)
        val alonBtn = findViewById<Button>(R.id.alone)
        val whoBtns = listOf(friendBtn,familyBtn,coupleBtn,alonBtn)
        var whoList = listOf("#친구랑","#가족들","#애인","#혼자서")



        for (btn in whoBtns) {
            btn.background = ContextCompat.getDrawable(this, R.drawable.buttonshape4)
            btn.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if(view.isSelected){
                            view.isSelected =false
                            who = ""
                        }
                        else {
                            view.isSelected = true // 버튼이 선택되었음을 나타내는 상태를 설정
                            who = whoList[whoBtns.indexOf(btn)]// 누른 버튼의 텍스트를 who 변수에 저장
                            Log.d("PLAN","who : "+who)

                            travelPlanManager.updatePlan(who=who)
                        }}
                }
                false
            }
        }






        val taxiBtn = findViewById<Button>(R.id.taxi)
        val busBtn = findViewById<Button>(R.id.bus)
        val carBtn = findViewById<Button>(R.id.car)
        val walkBtn = findViewById<Button>(R.id.walk)


        val transportBtn  =  listOf( carBtn ,taxiBtn,busBtn,walkBtn)
        var transportList = listOf("자차","택시","버스","도보")

        for (btn in transportBtn){
            btn.background = ContextCompat.getDrawable(this, R.drawable.buttonshape4)
            btn.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (view.isSelected) {
                            view.isSelected = false
                            transport = ""
                        } else {
                            view.isSelected = true
                            transport = transportList[transportBtn.indexOf(btn)]
                            Log.d("PLAN","transport : $transport")

                            travelPlanManager.updatePlan(transport = transport)
                        }
                    }
                }
                false
            }
        }


        val theme1Btn = findViewById<Button>(R.id.theme1)
        val theme2Btn = findViewById<Button>(R.id.theme2)
        val theme3Btn = findViewById<Button>(R.id.theme3)
        val theme4Btn = findViewById<Button>(R.id.theme4)


        val themeBtn = listOf(theme1Btn,theme2Btn,theme3Btn,theme4Btn)
        var themeList = listOf("1","2","3","4")


        for (btn in themeBtn){
            btn.background = ContextCompat.getDrawable(this, R.drawable.buttonshape4)
            btn.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (view.isSelected) {
                            view.isSelected = false
                            theme = ""
                        } else {
                            view.isSelected = true
                            theme = themeList[themeBtn.indexOf(btn)]
                            Log.d("PLAN","theme : $theme")

                            travelPlanManager.updatePlan(theme = theme)

                        }
                }
                false
            }
        }

        val lowBtn = findViewById<Button>(R.id.low_activity)
        val normalBtn = findViewById<Button>(R.id.normal_activity)
        val hardBtn = findViewById<Button>(R.id.hard_activity)
        val activityBtn = listOf(lowBtn,normalBtn,hardBtn)
        var activityList = listOf("적음","보통","많음")

        for (btn in activityBtn){
            btn.background = ContextCompat.getDrawable(this, R.drawable.buttonshape4)
            btn.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (view.isSelected) {
                            view.isSelected = false
                            activity = ""
                        } else {
                            view.isSelected = true
                            activity= activityList[activityBtn.indexOf(btn)] // 누른 버튼의 텍스트를 who 변수에 저장
                            Log.d("PLAN","activity : $activity")

                            travelPlanManager.updatePlan(activity = activity)
                        }}
                }
                false
            }
        }





        val helptxt = findViewById<ImageView>(R.id.helptxt)
        binding.help.setOnClickListener{
            helptxt.visibility = View.VISIBLE
        }


        binding.backgroundLayout.setOnClickListener {
            // 배경 레이아웃 클릭 시 이미지뷰를 숨김
            helptxt.visibility = View.GONE
        }

        binding.backBtn.setOnClickListener {
            finish()

        }





        binding.nextbutton.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("travelPlan", travelPlanManager.getPlan())
            startActivity(intent)
        }




    }





}