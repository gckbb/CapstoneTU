package com.example.kakaotest.Map

import android.content.Context
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.R
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapPolyLine

class SecondRoute : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_route)

        val secondList = intent.getParcelableArrayListExtra<SearchRouteData>("secondList")
        Log.d("PLAN","secondList \n"+ secondList.toString())

        val pointList = ArrayList<TMapPoint>()
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("app_key", "8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP")
            .apply()


        val backBtn = findViewById<ImageButton>(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()

        }

        // 값을 가져옴
        val appKey: String? = sharedPreferences.getString("app_key", null)
        val container: FrameLayout = findViewById(R.id.tmapViewContainer)
        val tMapView = TMapView(this)
        val tMapData = TMapData()
        val tMapGps = TMapPoint()

        tMapView.setSKTMapApiKey(appKey)
        container.addView(tMapView)


        tMapView.setOnMapReadyListener(object : TMapView.OnMapReadyListener {
            override fun onMapReady() {
                // 맵 로딩 완료 후 구현
                Toast.makeText(this@SecondRoute, "MapLoading", Toast.LENGTH_SHORT).show()
                //지도 중심좌표로 이동
                tMapView.setCenterPoint(37.566474, 126.985022);
                tMapView.zoomLevel = 10


                val iconList = listOf(
                    BitmapFactory.decodeResource(resources, R.drawable.start),
                    BitmapFactory.decodeResource(resources, R.drawable.pass),
                    BitmapFactory.decodeResource(resources, R.drawable.end)
                )



                for ((index, selectedPlace) in secondList!!.withIndex()) {
                    val tpoint = selectedPlace.pointdata!!.tpoint

                    //선택된 장소들 표시
                    if (tpoint != null) {
                        val marker = TMapMarkerItem().apply {
                            id = selectedPlace.pointdata.placeName
                            setTMapPoint(TMapPoint(tpoint.latitude, tpoint.longitude))
                            icon = if (index==0) iconList[0]
                            else if (index == secondList.size-1) iconList[2]
                            else iconList[1]

                        }
                        tMapView.addTMapMarkerItem(marker)
                    }


                }


                // 선택된 장소들의 TMapPoint를 이용하여 리스트 생성
                for (selectedPlace in secondList!!) {
                    selectedPlace.pointdata?.tpoint?.let { tMapPoint ->
                        pointList.add(tMapPoint)
                    }
                }



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
                }

            }})}}
