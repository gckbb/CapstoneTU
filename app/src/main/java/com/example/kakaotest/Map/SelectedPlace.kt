package com.example.kakaotest.Map


import android.annotation.SuppressLint
import android.content.Context
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
import com.skt.tmap.TMapPoint


class SelectedPlace : AppCompatActivity() {
    private val routetest = MakeRoute()
    private val logList = mutableListOf<String>()
    @SuppressLint("MissingInflatedId")
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
        val selectedPlaceNames = receivedDataList?.map { "${it.placeName}" }?.toMutableList() ?: mutableListOf()


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
            Log.d("add_test",receivedDataList.toString())
        }


        val foundListView: ListView = findViewById(R.id.foundListView)

        // SharedPreferences 객체 가져오기
        val sharedPreferences = getSharedPreferences("MySavedRestaurants", Context.MODE_PRIVATE)

        // SharedPreferences에서 모든 저장된 음식점 정보를 가져오기
        val savedRestaurantMap = sharedPreferences.all

        // 저장된 음식점 이름을 담을 리스트 생성
        val savedRestaurantNames = mutableListOf<String>()

        // SharedPreferences에 저장된 각 음식점에 대해 반복하여 가게 이름을 리스트에 추가
        for ((key, value) in savedRestaurantMap) {
            savedRestaurantNames.add(value.toString())
        }

        // foundListView의 아이템 클릭 리스너 설정
        foundListView.setOnItemClickListener { parent, view, position, id ->
            // 클릭한 위치(position)에 해당하는 아이템 가져오기
            val clickedRestaurantName = savedRestaurantNames[position]

            // 해당 음식점의 정보를 SharedPreferences에서 가져오기
            val sharedPreferences = getSharedPreferences("MySavedRestaurants", Context.MODE_PRIVATE)
            val clickedRestaurantMap = sharedPreferences.all
            // SharedPreferences에서 해당 음식점의 정보를 찾아서 receivedDataList에 추가
            for ((key, value) in clickedRestaurantMap) {
                if (value == clickedRestaurantName) {
                    // 해당 음식점의 정보를 receivedDataList에 추가
                    val latitudeLongitude = key.split("_")
                    val latitude = latitudeLongitude[1].toDouble()
                    val longitude = latitudeLongitude[2].toDouble()
                    val address = latitudeLongitude[3]
                    receivedDataList?.add(SelectedPlaceData(clickedRestaurantName, TMapPoint(longitude,latitude),address))

                    selectedPlaceNames.add(clickedRestaurantName)
                    nameAdapter.notifyDataSetChanged()
                    break
                }
            }
        }

        // 리스트를 ListView에 표시
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, savedRestaurantNames)
        foundListView.adapter = adapter

    }

}