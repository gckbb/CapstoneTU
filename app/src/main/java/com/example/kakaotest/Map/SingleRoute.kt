package com.example.kakaotest.Map

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.metaRoute.MetaDayRoute
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.R
<<<<<<< HEAD
import com.google.android.gms.maps.OnMapReadyCallback
=======
>>>>>>> other-origin/K_Ho_demo2
import com.google.gson.Gson
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapPolyLine



class SingleRoute : AppCompatActivity(){
   private lateinit var dayList:ArrayList<SearchRouteData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_route)

        val gson = Gson()

        val travelPlan = intent.getParcelableExtra<TravelPlan>("travelPlan")
        intent.getParcelableArrayListExtra<SearchRouteData>("dayList")?.let { dayListdata ->
            dayList = dayListdata
            Log.d("PLAN","firstRoute \n"+ dayList.toString())
        }
        val timeindex = intent.getIntExtra("time",0)
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
                Toast.makeText(this@SingleRoute, "MapLoading", Toast.LENGTH_SHORT).show()
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
                tMapView.removeAllTMapPolyLine()
                tMapView.removeAllTMapMarkerItem()
                for(i in timeindex until timeindex+2) {
                    val tpoint = dayList?.get(i)?.pointdata

                    if(tpoint != null) {
                        val marker = TMapMarkerItem().apply {
                            id = tpoint.placeName
                            setTMapPoint(TMapPoint(tpoint.tpoint.latitude, tpoint.tpoint.longitude))
                            icon = iconList[i]
                        }
                        tMapView.addTMapMarkerItem(marker)
                    }
                }


                // 선택된 장소들의 TMapPoint를 이용하여 리스트 생성
                for (selectedPlace in dayList!!) {
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

                        polyLines = tMapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH,dayList?.get(timeindex)?.pointdata?.tpoint,dayList?.get(timeindex+1)?.pointdata?.tpoint)
                        polyLines.setID("polylines${timeindex}")
                        polyLines.setLineColor(colorList[timeindex])
                        tMapView.addTMapPolyLine(polyLines)
                        val info = tMapView.getDisplayTMapInfo(polyLines.linePointList)
                        tMapView.zoomLevel = info.zoom
                        tMapView.setCenterPoint(info.point.latitude, info.point.longitude)

                    } catch (e: Exception) {


                    }
                }.start()
            }})}}





