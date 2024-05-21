package com.example.kakaotest

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import com.example.kakaotest.Plan.PSearchRouteData
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapPolyLine


class FirstRoute : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_route)

        val firstList = intent.getParcelableArrayListExtra<PSearchRouteData>("firstList")
        Log.d("PLAN","firstRoute \n"+ firstList.toString())

        val pointList = ArrayList<TMapPoint>()
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("app_key", "8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP")
            .apply()

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
                Toast.makeText(this@FirstRoute, "MapLoading", Toast.LENGTH_SHORT).show()
                //지도 중심좌표로 이동
                tMapView.setCenterPoint(37.566474, 126.985022);
                tMapView.zoomLevel = 10


                val iconList = listOf(
                    BitmapFactory.decodeResource(resources, R.drawable.start),
                    BitmapFactory.decodeResource(resources, R.drawable.point1),
                    BitmapFactory.decodeResource(resources, R.drawable.point2),
                    BitmapFactory.decodeResource(resources, R.drawable.point3),
                    BitmapFactory.decodeResource(resources, R.drawable.point4),
                    BitmapFactory.decodeResource(resources, R.drawable.point5),
                    BitmapFactory.decodeResource(resources, R.drawable.point6),
                    BitmapFactory.decodeResource(resources, R.drawable.point7),
                    BitmapFactory.decodeResource(resources, R.drawable.point8),
                    BitmapFactory.decodeResource(resources, R.drawable.point9),
                    BitmapFactory.decodeResource(resources, R.drawable.point10),
                    BitmapFactory.decodeResource(resources, R.drawable.end)
                )






                // 선택된 장소들의 TMapPoint를 이용하여 리스트 생성
                for (selectedPlace in firstList!!) {
                    selectedPlace.pointdata?.tpoint?.let { tMapPoint ->
                        pointList.add(tMapPoint)
                    }
                }


                // 선택된 장소들 간의 경로 표시
                Thread {
                    try {
                        var start: TMapPoint
                        var end: TMapPoint
                        var polyLines: TMapPolyLine
                        var polyLineList: ArrayList<TMapPolyLine> = arrayListOf<TMapPolyLine>()
                        var passList: ArrayList<TMapPoint> = arrayListOf<TMapPoint>()
                        var colorList  = arrayOf(Color.YELLOW,Color.BLUE,Color.GREEN,Color.MAGENTA,Color.CYAN,Color.RED,Color.TRANSPARENT)
                        for (i in 1 until pointList.size) {
                            passList.add(pointList[i])
                            polyLines = tMapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH,pointList[i-1],pointList[i])
                            polyLines.setID("polylines${i}")
                            polyLines.setLineColor(colorList[i%7])
                            polyLineList.add(polyLines)
                            if (polyLineList[i-1] != null) {
                                tMapView.addTMapPolyLine(polyLineList[i-1])
                                val info = tMapView.getDisplayTMapInfo(polyLineList[i-1].linePointList)
                                tMapView.zoomLevel = info.zoom
                                tMapView.setCenterPoint(info.point.latitude, info.point.longitude)
                            }
                        }
                        //start = pointList[0]
                        //end = pointList.last()

                        //polyLines = tMapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH,start,end,passList,2)
/*
                        if (polyLines != null) {
                            tMapView.addTMapPolyLine(polyLines)
                            val info = tMapView.getDisplayTMapInfo(polyLines.linePointList)
                            tMapView.zoomLevel = info.zoom
                            tMapView.setCenterPoint(info.point.latitude, info.point.longitude)




                        } else {


                        }
                        */



                    } catch (e: Exception) {


                    }
                }.start()

                for ((index, selectedPlace) in firstList!!.withIndex()) {
                    val tpoint = selectedPlace.pointdata!!.tpoint

                    //선택된 장소들 표시
                    if (tpoint != null) {
                        val marker = TMapMarkerItem().apply {
                            id = selectedPlace.pointdata.placeName
                            setTMapPoint(TMapPoint(tpoint.latitude, tpoint.longitude))
                            icon = if (index==0) iconList[0]
                            else if (index == firstList.size-1) iconList[11]
                            else iconList[index]

                        }
                        tMapView.addTMapMarkerItem(marker)
                    }


                }




            }})}}

