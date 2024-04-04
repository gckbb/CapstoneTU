package com.example.kakaotest.Map


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.kakaotest.Fragment.DatePickerFragment
import com.example.kakaotest.R
import com.google.firebase.firestore.FirebaseFirestore

import java.util.Calendar

class PlanInfoInput : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_info)
        Log.d("Plan","PlaninfoInput Activity success")
        // val selectedPlaces: Array<Place>? = intent.getParcelableArrayExtra("selectedPlaces") as? Array<Place>
        var selectedDate: Calendar = Calendar.getInstance()
        val where = findViewById<TextView>(R.id.where)
        val firstdate: TextView = findViewById<TextView>(R.id.day1txt)
        val seconddate: TextView = findViewById<TextView>(R.id.day2txt)
        val firstBtn = findViewById<Button>(R.id.day1btn)
        val secondBtn = findViewById<Button>(R.id.day2btn)



        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("travelPlans")
        val documentRef = collectionRef.document("travelId")




        /*어디로?*/
        var place : String = ""
        /*        selectedPlaces?.let {
                     place = selectedPlaces?.firstOrNull()?.placeName.toString()
                    where.text = place
                }*/

        Log.d("Plan","place : $place")
        /*언제?*/
        // 첫 번째 날짜 선택 버튼 클릭 이벤트 처리
        var  firstday : String = ""

        firstBtn.setOnClickListener {
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
        Log.d("Plan","firstday : $firstday ")
        var secondday : String = ""

        // 두 번째 날짜 선택 버튼 클릭 이벤트 처리
        secondBtn.setOnClickListener {
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
        val whoBtns = listOf(friendBtn, familyBtn, coupleBtn, alonBtn)
        var who: String = ""

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
                            who = (view as Button).text.toString() // 누른 버튼의 텍스트를 who 변수에 저장
                        }}
                }
                false
            }
        }
        Log.d("PLAN","who : $who")
        val taxiBtn = findViewById<Button>(R.id.taxi)
        val busBtn = findViewById<Button>(R.id.bus)
        val carBtn = findViewById<Button>(R.id.car)
        val walkBtn = findViewById<Button>(R.id.walk)


        val transportBtn  =  listOf( carBtn ,taxiBtn,busBtn,walkBtn)
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
                            transport = (view as Button).text.toString() // 누른 버튼의 텍스트를 who 변수에 저장
                        }
                    }
                }
                false
            }
        }
        Log.d("PLAN","transport : $transport")

        val theme1Btn = findViewById<Button>(R.id.theme1)
        val theme2Btn = findViewById<Button>(R.id.theme2)
        val theme3Btn = findViewById<Button>(R.id.theme3)
        val theme4Btn = findViewById<Button>(R.id.theme4)


        val themeBtn = listOf(theme1Btn,theme2Btn,theme3Btn,theme4Btn)
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
                            theme = (view as Button).text.toString() // 누른 버튼의 텍스트를 who 변수에 저장
                        }
                }
                false
            }
        }
        Log.d("PLAN","theme : $theme")
        val lowBtn = findViewById<Button>(R.id.low_activity)
        val normalBtn = findViewById<Button>(R.id.normal_activity)
        val hardBtn = findViewById<Button>(R.id.hard_activity)
        val activityBtn = listOf(lowBtn,normalBtn,hardBtn)
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
                            activity= (view as Button).text.toString() // 누른 버튼의 텍스트를 who 변수에 저장
                        }}
                }
                false
            }
        }
        Log.d("PLAN","activity : $activity")
/*
        val travelPlan = TravelPlan(
            where = null,
            startDate = firstday,
            endDate = secondday,
            who = who,
            transportion = transport,
            theme = theme,
            activity = activity,
            destinations = null
        )

        collectionRef
            .add(travelPlan)
            .addOnSuccessListener { documentReference ->
                Log.d("firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("firestore", "Error adding document", e)
            }
*/
    }






}