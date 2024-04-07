package com.example.kakaotest

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import com.example.kakaotest.Plan.DRouteData
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

        val tMapGps = TMapPoint()

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString("app_key", "8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP")
            .apply()


        // 값을 가져옴
        val appKey: String? = sharedPreferences.getString("app_key", null)
        val container: FrameLayout = findViewById(R.id.tmapViewContainer)
        val tMapView = TMapView(this)
        val tMapData = TMapData()



        tMapView.setSKTMapApiKey(appKey)
        container.addView(tMapView)

        tMapView.setOnMapReadyListener(object : TMapView.OnMapReadyListener {
            override fun onMapReady() {

                // FirstRoute 액티비티에서 인텐트에서 데이터 가져오기
                val tmapPointStringList = intent.getStringArrayListExtra("firstTpoint")

                if (tmapPointStringList != null) {
                    Log.d("PLAN", tmapPointStringList.get(0).toString())
                    Log.d("PLAN", tmapPointStringList.get(1).toString())
                    Log.d("PLAN", tmapPointStringList.get(2).toString())
                }

                var start: TMapPoint? = null
                var end: TMapPoint? = null
                var polyLines: TMapPolyLine
                var passList: ArrayList<TMapPoint> = arrayListOf<TMapPoint>()


                //intent에서 받아온 문자열을 tmappoint로 변환함.
                if (tmapPointStringList != null) {
                    for (i in 0 until tmapPointStringList.size) {
                        val parts = tmapPointStringList[i].split("=", ",", "}")
                        if (i == 0) {
                            start = TMapPoint(parts[1].toDouble(), parts[3].toDouble())
                            val marker = TMapMarkerItem().apply {
                                setTMapPoint(TMapPoint(parts[1].toDouble(), parts[3].toDouble()))
                                id = i.toString()
                                icon = BitmapFactory.decodeResource(resources, R.drawable.start)
                            }
                            tMapView.addTMapMarkerItem(marker)
                        } else if (i == tmapPointStringList.size - 1) {
                            end = TMapPoint(parts[1].toDouble(), parts[3].toDouble())
                            val marker = TMapMarkerItem().apply {
                                setTMapPoint(TMapPoint(parts[1].toDouble(), parts[3].toDouble()))
                                id = i.toString()
                                icon = BitmapFactory.decodeResource(resources, R.drawable.end)
                            }
                            tMapView.addTMapMarkerItem(marker)
                        } else {
                            passList.add(TMapPoint(parts[1].toDouble(), parts[3].toDouble()))
                            val marker = TMapMarkerItem().apply {
                                setTMapPoint(TMapPoint(parts[1].toDouble(), parts[3].toDouble()))
                                id = i.toString()
                                icon = BitmapFactory.decodeResource(resources, R.drawable.pass)
                            }
                            tMapView.addTMapMarkerItem(marker)
                        }
                    }
                }


                Thread {
                    try {
                        polyLines = tMapData.findPathDataWithType(
                            TMapData.TMapPathType.CAR_PATH,
                            start,
                            end,
                            passList,
                            2
                        )
                        tMapView.addTMapPolyLine(polyLines)
                        val info = tMapView.getDisplayTMapInfo(polyLines.linePointList)
                        tMapView.zoomLevel = info.zoom
                        tMapView.setCenterPoint(info.point.latitude, info.point.longitude)
                    } catch (e: Exception) {
                        Log.e("Error", "drawRoutePolyLine error")
                    }
                }.start()
// 가져온 문자열 리스트를 TMapPoint 리스트로 변환
                /*
                val tmapPointList = tmapPointStringList?.map {
                    val parts = it.split("=",",","}")
                    val latitude = parts[1].toDouble()
                    val longitude = parts[3].toDouble()
                    TMapPoint(latitude, longitude)
                } ?: emptyList()
        // TMapPoint 리스트를 순회하며 각 좌표에 마커를 추가
                tmapPointList.forEachIndexed { index, tMapPoint ->
                    // 마커 생성
                    val marker = TMapMarkerItem()
                    // 마커의 좌표 설정
                    marker.setTMapPoint(TMapPoint())

                    // 마커의 아이콘 설정 (예: 기본 마커 아이콘 사용)
                    marker.setIcon(BitmapFactory.decodeResource(resources, R.drawable.point))
                    // 마커의 타이틀 설정 (선택사항)
                    // 마커를 지도에 추가
                    tMapView.addTMapMarkerItem(marker)

                }
        */

                /*
                        val firstTpointList = intent.getStringArrayListExtra("firstTpoint")
                        if (firstTpointList != null) {
                            // 데이터가 제대로 전달되었을 때 처리
                            Log.d("FirstRoute", "firstDayList: $firstTpointList")

                            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            sharedPreferences.edit()
                                .putString("app_key", "8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP")
                                .apply()


                            // 값을 가져옴
                            val appKey: String? = sharedPreferences.getString("app_key", null)
                            val container: FrameLayout = findViewById(R.id.tmapViewContainer)
                            val tMapView = TMapView(this)
                            val tMapData = TMapData()



                            tMapView.setSKTMapApiKey(appKey)
                            container.addView(tMapView)

                            val routeTpointList = ArrayList<TMapPoint>()


                            //  Log.d("PLAN","pointList : ${pointList}")
                            val iconList = listOf(
                                BitmapFactory.decodeResource(resources, R.drawable.start),
                                BitmapFactory.decodeResource(resources, R.drawable.pass),
                                BitmapFactory.decodeResource(resources, R.drawable.end),

                                )
                            val alTMapPoint = ArrayList<TMapPoint>()
                            if (firstTpointList != null) {
                                for (item in firstTpointList) {
                                    val coords = item.split(",")
                                    if (coords.size == 2) {
                                        val latitude = coords[0].toDouble()
                                        val longitude = coords[1].toDouble()
                                        val tpoint = TMapPoint(latitude, longitude)
                                        alTMapPoint.add(tpoint)
                                    }
                                }
                            }


                            val tMapPolyLine = TMapPolyLine()
                            tMapPolyLine.lineColor = Color.BLUE
                            tMapPolyLine.lineWidth = 2f
                            for (i in alTMapPoint.indices) {
                                tMapPolyLine.addLinePoint(alTMapPoint[i])
                            }
                            val newPolyLine = TMapPolyLine("line1", alTMapPoint)
                            tMapView.addTMapPolyLine(newPolyLine)

                        } else {
                            // 데이터가 전달되지 않았거나 잘못된 키를 사용한 경우
                            Log.e("Error", "Failed to retrieve data")
                        }

                */
            }
        })
    }

    private fun drawRoutePolyLine(tmapPointStringList: ArrayList<String>) {
        var start: TMapPoint? = null
        var end: TMapPoint? = null
        var polyLines: TMapPolyLine
        val tMapView = TMapView(this)
        var passList: ArrayList<TMapPoint> = arrayListOf<TMapPoint>()
        val tMapData = TMapData()

        //intent에서 받아온 문자열을 tmappoint로 변환함.
        for (i in 0 until tmapPointStringList.size) {
            val parts = tmapPointStringList[i].split("=", ",", "}")
            if (i == 0) {
                start = TMapPoint(parts[1].toDouble(), parts[3].toDouble())
            } else if (i == tmapPointStringList.size - 1) {
                end = TMapPoint(parts[1].toDouble(), parts[3].toDouble())
            } else {
                passList.add(TMapPoint(parts[1].toDouble(), parts[3].toDouble()))
            }
        }
        Thread {
            try {
                polyLines = tMapData.findPathDataWithType(
                    TMapData.TMapPathType.CAR_PATH,
                    start,
                    end,
                    passList,
                    2
                )
                tMapView.addTMapPolyLine(polyLines)
                val info = tMapView.getDisplayTMapInfo(polyLines.linePointList)
                tMapView.zoomLevel = info.zoom
                tMapView.setCenterPoint(info.point.latitude, info.point.longitude)
            } catch (e: Exception) {
                Log.e("Error", "drawRoutePolyLine error")
            }
        }.start()

    }
}