package com.example.kakaotest.Map


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.example.kakaotest.R
import com.example.kakaotest.Utility.tmap.MakeRoute


class SelectedPlace : AppCompatActivity() {
    private val routetest = MakeRoute()
    private val logList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_place)


        val receivedDataList =
            intent.getParcelableArrayListExtra<SelectedPlaceData>("selectedPlaceDataList")
        val travelPlan = intent.getParcelableExtra<TravelPlan>("travelPlan")


        val backBtn = findViewById<ImageButton>(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()

        }


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


        // next 버튼 클릭 시 RouteListActivity 로 이동
//        val nextButton: Button = findViewById(R.id.nextbutton)
//        nextButton.setOnClickListener {
//            val intent = Intent(this, RouteListActivity::class.java)
//            intent.putExtra("travelPlan", travelPlan)
//            intent.putParcelableArrayListExtra("selectedPlaceDataList", ArrayList(receivedDataList))
//            startActivity(intent)
//            Log.d("Item", receivedDataList.toString())
//        }
        // next 버튼 클릭 시 CreatedPath 로 이동
        val nextButton: Button = findViewById(R.id.nextbutton)
        nextButton.setOnClickListener {
            val intent = Intent(this, FoodSelectActivity::class.java)
            intent.putExtra("travelPlan", travelPlan)
            intent.putParcelableArrayListExtra("selectedPlaceDataList", ArrayList(receivedDataList))
            startActivity(intent)
            Log.d("Item", receivedDataList.toString())
        }


    }

}