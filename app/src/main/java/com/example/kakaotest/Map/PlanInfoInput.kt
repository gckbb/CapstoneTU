package com.example.kakaotest.Map


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.example.kakaotest.DataModel.Place
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.Fragment.DatePickerFragment
import com.example.kakaotest.R
import com.example.kakaotest.databinding.ActivityMapBinding
import com.example.kakaotest.databinding.ActivityPlanInfoBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon
import com.skydoves.balloon.overlay.BalloonOverlayCircle

import java.util.Calendar


 class PlanInfoInput : AppCompatActivity() {
     private val travelPlanList = mutableListOf<TravelPlan>()
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

        val firstdate: TextView = findViewById<TextView>(R.id.day1txt)
        val seconddate: TextView = findViewById<TextView>(R.id.day2txt)

        Log.d("Plan","place : $selectedPlace")
        /*언제?*/
        // 첫 번째 날짜 선택 버튼 클릭 이벤트 처리
        var  firstday : String = ""

        binding.day1btn.setOnClickListener {
            val dialogFragment = DatePickerFragment { year, month, day ->
                if (month+1 >= 10 ){
                    if (day >= 10 ){
                        firstday = "$year/${month + 1}/$day"
                    } else{
                        firstday = "$year/${month + 1}/0$day"
                    }
                } else{
                    if (day >= 10 ){
                        firstday = "$year/0${month + 1}/$day"
                    }else{
                        firstday = "$year/0${month + 1}/0$day"
                    }}
                firstdate.text =  firstday // 두 번째 날짜 TextView에 선택된 날짜 설정

            }
            dialogFragment.show(supportFragmentManager, "firstDatePicker")
        }


        Log.d("Plan","firstday : $firstdate ")
        var secondday : String = ""

        // 두 번째 날짜 선택 버튼 클릭 이벤트 처리
        binding.day2btn.setOnClickListener {
            val dialogFragment = DatePickerFragment { year, month, day ->
                if (month+1 >= 10 ){
                    if (day >= 10 ){
                        secondday= "$year/${month + 1}/$day"
                    } else{
                        secondday= "$year/${month + 1}/0$day"
                    }
                } else{
                    if (day >= 10 ){
                        secondday= "$year/0${month + 1}/$day"
                    }else{
                        secondday= "$year/0${month + 1}/0$day"
                    }}
                seconddate.text = secondday // 두 번째 날짜 TextView에 선택된 날짜 설정
            }
            dialogFragment.show(supportFragmentManager, "secondDatePicker")
        }




        val friendBtn: Button = findViewById(R.id.friend)
        val familyBtn = findViewById<Button>(R.id.family)
        val coupleBtn= findViewById<Button>(R.id.couple)
        val alonBtn = findViewById<Button>(R.id.alone)
        val whoBtns = listOf(friendBtn,familyBtn,coupleBtn,alonBtn)
        var whoList = listOf("친구","가족","애인","혼자")


        var who=""
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
        var transport : String = ""
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


        var theme : String = ""
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
        var activity : String = ""
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


        // 여행 계획 정보 생성

        val selectedDate1 = binding.day1txt.text.toString()
        val selectedDate2 = binding.day2txt.text.toString()
        val selectedWho = who // 이전 코드에서 who 변수를 설정하는 부분이 있어야 함
        val transportation = transport // 이전 코드에서 transport 변수를 설정하는 부분이 있어야 함
        val themes = theme // 이전 코드에서 theme 변수를 설정하는 부분이 있어야 함
        val activityLevel = activity // 이전 코드에서 activity 변수를 설정하는 부분이 있어야 함

        val travelPlan = TravelPlan(
            where = selectedPlace,
            startDate = selectedDate1,
            endDate = selectedDate2,
            who = selectedWho,
            transportion = transportation,
            theme = themes,
            activity = activityLevel,
            destinations = listOf() // 목적지는 비어있는 리스트로 초기화하거나 필요에 따라 적절한 값으로 초기화해야 합니다.
        )



        binding.nextbutton.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("travel_plan", travelPlan)
            Log.d("PLAN", travelPlan.toString())
            startActivity(intent)
        }

    }




}

