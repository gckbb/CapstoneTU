package com.example.kakaotest.Map

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.R
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapPolyLine


class FirstRoute : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_route)

        val firstList = intent.getParcelableArrayListExtra<SearchRouteData>("dayList")
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


                // 선택된 장소들의 TMapPoint를 이용하여 리스트 생성
                for (selectedPlace in firstList!!) {
                    selectedPlace.pointdata?.tpoint?.let { tMapPoint ->
                        pointList.add(tMapPoint)
                    }
                }




                Thread {
                    try {
                        var start: TMapPoint
                        var end: TMapPoint
                        var polyLines: TMapPolyLine
                        var polyLineList: ArrayList<TMapPolyLine> = arrayListOf<TMapPolyLine>()
                        var passList: ArrayList<TMapPoint> = arrayListOf<TMapPoint>()
                        var colorList  = arrayOf(
                            Color.rgb(255,191,0),
                            Color.rgb(153,204,255),
                            Color.rgb(63,232,127),
                            Color.rgb(255,192,203),
                            Color.rgb(92,255,209),
                            Color.rgb(255,105,180),
                            Color.rgb(121,236,255),
                            Color.rgb(255,127,0)
                        )
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
                    } catch (e: Exception) {


                    }
                }.start()
            }})}}




