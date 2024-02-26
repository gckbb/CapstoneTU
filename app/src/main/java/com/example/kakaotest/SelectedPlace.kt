package com.example.kakaotest

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.HashMap


class SelectedPlace : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_place)

       // val documnetID = SavedUser().getUserDataFromSharedPreferences(this) //회원정보 문서 ID

        // 전달된 데이터 받기
        val selectedPlacesList = intent.getParcelableArrayListExtra<SearchData>("selectedPlacesList")


        // ListView 참조
        val placeListView: ListView = findViewById(R.id.placeListView)

        // 어댑터 생성 및 설정
        val selectedPlaceNames = selectedPlacesList?.map { "${it.id}"} ?: emptyList()


        // 어댑터 생성
        val nameadapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,selectedPlaceNames)

        // ListView에 어댑터 설정
        placeListView.adapter = nameadapter






        // next 버튼 클릭 시 CreatedPath 로 이동
        val nextButton: Button = findViewById(R.id.nextbutton)
        nextButton.setOnClickListener {
            val intent = Intent(this, CreatedPath::class.java)
            intent.putParcelableArrayListExtra("selectedPlacesList", ArrayList(selectedPlacesList))
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
