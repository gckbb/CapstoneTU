package com.example.kakaotest.Map


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.Map.FoodSelectActivity
import com.example.kakaotest.CreatedRoute1
import com.example.kakaotest.Plan.PMakeRoute
import com.example.kakaotest.Plan.SelectedPlaceData
import com.example.kakaotest.R
import java.util.ArrayList


class SelectedPlace : AppCompatActivity() {
    private val routetest = PMakeRoute()
    private val logList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_place)


        val receivedDataList =
            intent.getParcelableArrayListExtra<SelectedPlaceData>("selectedPlaceDataList")

        //    Log.d("PLAN", "receivedDataList : ${receivedDataList.toString()}")
        // 다른 클래스의 함수 호출하여 로그를 가져옴





        // val documnetID = SavedUser().getUserDataFromSharedPreferences(this) //회원정보 문서 ID


        // ListView 참조
        val placeListView: ListView = findViewById(R.id.placeListView)

        // 어댑터 생성 및 설정
        val selectedPlaceNames = receivedDataList?.map { "${it.placeName}" } ?: emptyList()

        // 어댑터 생성
        val nameAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, selectedPlaceNames)

        // ListView에 어댑터 설정
        placeListView.adapter = nameAdapter

        // 로그에 selectedPlaceNames 출력
        Log.d("selectedPlaceNames", selectedPlaceNames.toString())


        // next 버튼 클릭 시 CreatedPath 로 이동
        val nextButton: Button = findViewById(R.id.nextbutton)
        nextButton.setOnClickListener {
            val intent = Intent(this, FoodSelectActivity::class.java)
            intent.putParcelableArrayListExtra("selectedPlaceDataList", ArrayList(receivedDataList))
            startActivity(intent)
            Log.d("Item", receivedDataList.toString())
        }


    }

}