package com.example.kakaotest.TourApi

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.DataModel.Restaurant
import com.example.kakaotest.Utility.tmap.ApiAdapter
import com.example.kakaotest.R
import com.example.kakaotest.databinding.ActivityDetailMapBinding
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem

class Detail_map_test : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailMapBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("app_key", "8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP").apply()

        // 값을 가져옴
        val appKey: String? = sharedPreferences.getString("app_key", null)

        // FrameLayout 컨테이너를 XML에서 찾아옴
        val container: FrameLayout = findViewById(R.id.detailView)

        // TMapView 인스턴스를 생성
        val tMapView = TMapView(this@Detail_map_test)
        val tMapData = TMapData()
        val tMapGps = TMapPoint()
        val apiAdapter = ApiAdapter()

        // TMapView를 FrameLayout에 추가
        container.addView(tMapView)
        // 발급받은 키로 TMapView에 API 키 설정
        tMapView.setSKTMapApiKey(appKey)

        val selected_place = intent.getSerializableExtra("restaurant") as? Restaurant



        tMapView.setOnMapReadyListener(object : TMapView.OnMapReadyListener {
            override fun onMapReady() {
                // 맵 로딩이 완료된 후에 수행할 동작을 구현해주세요
                // 예: 마커 추가, 경로 표시 등
                Toast.makeText(this@Detail_map_test, "MapLoading", Toast.LENGTH_SHORT).show()

                val marker = TMapMarkerItem()

                if (selected_place != null) {
                    Log.d("restaurant","${selected_place.title}")
                    tMapView.setCenterPoint(selected_place.mapx.toDouble(),selected_place.mapy.toDouble())
                    marker.setTMapPoint(selected_place.mapx.toDouble(),selected_place.mapy.toDouble())
                }
                tMapView.zoomLevel = 15


                marker.id = "marker1"
                marker.icon = BitmapFactory.decodeResource(resources, R.drawable.point)
                tMapView.addTMapMarkerItem(marker)


            }
        })
    }

}