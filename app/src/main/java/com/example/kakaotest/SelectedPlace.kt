package com.example.kakaotest

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.Plan.PMakeRoute
import com.example.kakaotest.Plan.SelectedPlaceData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList
import java.util.HashMap


class SelectedPlace : AppCompatActivity() {
    private val routetest = PMakeRoute()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_place)


        val receivedDataList =
            intent.getParcelableArrayListExtra<SelectedPlaceData>("selectedPlaceDataList")

        Log.d("receivedDataList", receivedDataList.toString())


        // 여기서 selectedPlaceData를 사용할 수 있습니다.
        if (receivedDataList != null) {
            val startPoint: SelectedPlaceData? = receivedDataList.firstOrNull()


            if (startPoint != null) {
                Log.d("SelectedPlace", "Start Point: $startPoint")
                routetest.routeSet(receivedDataList, startPoint)

                // routeStart 메소드 호출 및 로그 출력
                routetest.routeStart(2, 6)
                Log.d("SelectedPlace", "Route Started")

                // printTotalRoute 메소드 호출 및 로그 출력
                routetest.printTotalRoute()
                Log.d("SelectedPlace", "Total Route Printed")
            } else {
                Log.e("Error", "startPoint is null")


            }


            // val documnetID = SavedUser().getUserDataFromSharedPreferences(this) //회원정보 문서 ID

            // 전달된 데이터 받기
            //   val selectedPlacesList = intent.getParcelableArrayListExtra<SearchData>("selectedPlacesList")

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
                val intent = Intent(this, CreatedPath::class.java)
                intent.putParcelableArrayListExtra("selectedPlaceDataList", receivedDataList)
                startActivity(intent)
            }


        }

        fun apiRequest(startX: Number, startY: Number, endX: Number, endY: Number): Number? {
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://apis.openapi.sk.com/tmap/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService: ApiService = retrofit.create(ApiService::class.java)

            var input = HashMap<String, Any>()
            input["startY"] = startY
            input["startX"] = startX
            input["endY"] = endY
            input["endX"] = endX
            input["totalValue"] = 2

            val routeCall = apiService.getRoute(input)
            var totalTime: Number? = null

            routeCall.enqueue(object : Callback<FeatureCollection> {
                override fun onResponse(
                    call: Call<FeatureCollection>,
                    response: Response<FeatureCollection>
                ) {
                    val RouteInfo: FeatureCollection? = response.body()
                    Log.d(ContentValues.TAG, "getPostList onResponse()")
                    Log.d(ContentValues.TAG, "type : ${RouteInfo?.features?.get(0)?.type}")
                    Log.d(
                        ContentValues.TAG,
                        "TotalTime : ${RouteInfo?.features?.get(0)?.properties?.totalTime}"
                    )
                    Log.d(
                        ContentValues.TAG,
                        "TotalDistance : ${RouteInfo?.features?.get(0)?.properties?.totalDistance}"
                    )
                    Log.d(
                        ContentValues.TAG,
                        "TotalFare : ${RouteInfo?.features?.get(0)?.properties?.totalFare}"
                    )
                    totalTime = RouteInfo?.features?.get(0)?.properties?.totalTime
                }

                override fun onFailure(call: Call<FeatureCollection>, t: Throwable) {
                    call.cancel()
                    Log.d(ContentValues.TAG, "api fail")
                    totalTime = null
                }

            })
            return totalTime
        }
    }
}