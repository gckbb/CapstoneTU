package com.example.kakaotest

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast

import com.example.kakaotest.Plan.DRouteData
import com.example.kakaotest.Plan.SelectedPlace
import com.example.kakaotest.Plan.SelectedPlaceData
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapPolyLine
import com.skt.tmap.poi.TMapPOIItem


class FirstRoute : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_route)
        val pointList = ArrayList<TMapPoint>()
        // SharedPreferences를 사용하여 API 키 가져오기
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val appKey: String? = sharedPreferences.getString("app_key", null)

        // TMapView 초기화
        val container: FrameLayout = findViewById(R.id.tmapViewContainer)
        val tMapView = TMapView(this)
        tMapView.setSKTMapApiKey(appKey)
        container.addView(tMapView)

        // Intent에서 ParcelableArrayListExtra로 선택된 장소 목록 가져오기
        val selectedPlaceList = intent.getParcelableArrayListExtra<SelectedPlace>("selectedPlaceList")

        Log.d("PLAN", selectedPlaceList.toString())
        // 선택된 장소 목록이 유효한 경우 지도에 마커 표시
        if (selectedPlaceList != null) {
            for (selectedPlace in selectedPlaceList) {
                Log.d("PLAN", "Selected Place:\n $selectedPlace")
                val tmapPoint = selectedPlace.tmapPoint
                val latitude = tmapPoint.latitude
                val longitude = tmapPoint.longitude
             if(tmapPoint != null) {
                // 마커 아이콘 설정
                val iconList = listOf(
                    BitmapFactory.decodeResource(resources, R.drawable.start),
                    BitmapFactory.decodeResource(resources, R.drawable.pass),
                    BitmapFactory.decodeResource(resources, R.drawable.end)
                )
                // 마커 추가
                val marker = TMapMarkerItem().apply {
                    tMapPoint = TMapPoint(latitude, longitude)
                    icon = iconList[1] // 중간 지점 마커 아이콘
                }

                tMapView.addTMapMarkerItem(marker)

                Log.d("PLAN", "pointList : \n"+pointList.toString())
             }
            }
        }

        for (selectedPlace in selectedPlaceList!!) {
            val tmapPoint = selectedPlace.tmapPoint
            tmapPoint?.let { tMapPoint ->
                pointList.add(tMapPoint)
            }
        }
    }
/*
        // 선택된 장소들 간의 경로 표시
        for (i in 0 until pointList.size - 1) {
            val start = pointList[i]
            val end = pointList[i + 1]



            TMapData().findPathData(start, end) { tMapPolyLine ->

                val newPolyLine = TMapPolyLine("line1", pointList)

                // 지도에 추가
                tMapView.addTMapPolyLine(newPolyLine)

                // 경로 추가 후 지도의 중심 및 줌 조절
                val info = tMapView.getDisplayTMapInfo(newPolyLine.linePointList)
                tMapView.zoomLevel = info.zoom
                tMapView.setCenterPoint(info.point.latitude, info.point.longitude)
            }
        }*/

    }





