package com.example.kakaotest.Map

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.metaRoute.MetaDayRoute
import com.example.kakaotest.DataModel.metaRoute.SearchMetaData
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.R
import com.example.kakaotest.Utility.SharedPreferenceUtil
import com.google.gson.Gson
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapPolyLine
import java.util.LinkedList
import com.google.gson.reflect.TypeToken


class FirstRoute : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_route)

        val gson = Gson()
     //   val travelPlan = intent.getParcelableExtra<TravelPlan>("travelPlan")
        lateinit var firstList:ArrayList<SearchRouteData>
        lateinit var firstList2:MetaDayRoute


        val travelPlan= SharedPreferenceUtil.getTravelPlanFromSharedPreferences(this)

        val backic = findViewById<ImageButton>(R.id.back_btn)
        backic.setOnClickListener{
            finish()
        }

        intent.getStringExtra("firstList2")?.let { firstListdata2 ->
            firstList2 = gson.fromJson(firstListdata2, MetaDayRoute::class.java)
            Log.d("PLAN","firstRoute \n"+ firstList2.toString())
        }
        intent.getParcelableArrayListExtra<SearchRouteData>("firstList")?.let { firstListdata ->
            firstList = firstListdata
            Log.d("PLAN","firstRoute \n"+ firstList.toString())
        }


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
                tMapView.removeAllTMapPolyLine()
                tMapView.removeAllTMapMarkerItem()

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
                    BitmapFactory.decodeResource(resources, R.drawable.end),
                    BitmapFactory.decodeResource(resources, R.drawable.busmarker),
                    BitmapFactory.decodeResource(resources, R.drawable.subwaymarker),
                    BitmapFactory.decodeResource(resources, R.drawable.walkmarker)
                )

                if(travelPlan?.transportion == "버스") {
                    Thread {
                        try {
                            for (i in 1 until firstList2.dayRoute.size) {
                                var polylineindex = 1
                                var polyLines: TMapPolyLine
                                var tpointList: ArrayList<TMapPoint> = ArrayList<TMapPoint>()
                                var tpointPathList: ArrayList<TMapPoint> = ArrayList<TMapPoint>()
                                tpointList.add(
                                    TMapPoint(
                                        firstList2.dayRoute?.get(i)?.metaData?.requestParameters?.startY?.toDouble()!!,
                                        firstList2.dayRoute?.get(i)?.metaData?.requestParameters?.startX?.toDouble()!!
                                    )
                                )
                                var dashStyle: IntArray = IntArray(2)  // 점선
                                dashStyle[0] = 10
                                dashStyle[1] = 20
                                var dashStyle2: IntArray = IntArray(2) // 실선
                                dashStyle.plus(0)
                                dashStyle.plus(0)
                                for (selectedRoute in firstList2?.dayRoute?.get(i)?.metaData?.plan?.itineraries?.get(0)?.legs!!) {
                                    if (selectedRoute.mode == "WALK") {
                                        if(polylineindex != 1) {
                                            val marker = TMapMarkerItem().apply {
                                                id = "indeex${polylineindex}${i}"
                                                setTMapPoint(
                                                    TMapPoint(
                                                        selectedRoute.start?.lat?.toDouble()!!,
                                                        selectedRoute.start?.lon?.toDouble()!!
                                                    )
                                                )
                                                icon = iconList[14]
                                            }
                                            tMapView.addTMapMarkerItem(marker)
                                        }

                                        tpointList.add(
                                            TMapPoint(
                                                selectedRoute.end?.lat?.toDouble()!!,
                                                selectedRoute.end?.lon?.toDouble()!!
                                            )
                                        )
                                        polyLines = tMapData.findPathDataWithType(
                                            TMapData.TMapPathType.PEDESTRIAN_PATH,
                                            TMapPoint(
                                                selectedRoute.start?.lat?.toDouble()!!,
                                                selectedRoute.start?.lon?.toDouble()!!
                                            ),
                                            TMapPoint(
                                                selectedRoute.end?.lat?.toDouble()!!,
                                                selectedRoute.end?.lon?.toDouble()!!
                                            )
                                        )
                                        polyLines.setID("polylin${polylineindex}${i}")
                                        polyLines.pathEffect = dashStyle
                                        polyLines.setLineColor(Color.YELLOW)
                                        tMapView.addTMapPolyLine(polyLines)
                                        polylineindex++
                                    } else if (selectedRoute.mode == "BUS" || selectedRoute.mode == "SUBWAY") {
                                        val marker = TMapMarkerItem().apply {
                                            id = "index${polylineindex}${i}"
                                            setTMapPoint(
                                                TMapPoint(
                                                    selectedRoute.start?.lat?.toDouble()!!,
                                                    selectedRoute.start?.lon?.toDouble()!!
                                                )
                                            )
                                            icon = if (selectedRoute.mode == "BUS") iconList[12]
                                            else iconList[13]
                                        }
                                        tMapView.addTMapMarkerItem(marker)

                                        tpointList?.add(
                                            TMapPoint(
                                                selectedRoute.start?.lat?.toDouble()!!,
                                                selectedRoute.start?.lon?.toDouble()!!
                                            )
                                        )
                                        /*
                                    polyLines = tMapData.findPathDataWithType(
                                        TMapData.TMapPathType.CAR_PATH,
                                        TMapPoint(
                                            selectedRoute.passStopList?.stationList.get(i - 1).lat.toDouble(),
                                            selectedRoute.passStopList?.stationList.get(i - 1).lon.toDouble()
                                        ),
                                        TMapPoint(
                                            selectedRoute.passStopList?.stationList.get(i).lat.toDouble(),
                                            selectedRoute.passStopList?.stationList.get(i).lon.toDouble()
                                        )
                                    )


                                     */


                                        val split =
                                            selectedRoute.passShape?.linestring?.split(",", " ")
                                        for (j in 0 until split?.size!! / 2) {
                                            tpointPathList.add(
                                                TMapPoint(
                                                    split.get(j * 2 + 1).toDouble(),
                                                    split.get((j * 2)).toDouble()
                                                )
                                            )
                                        }
                                        tpointPathList.add(
                                            TMapPoint(
                                                selectedRoute.end?.lat?.toDouble()!!,
                                                selectedRoute.end?.lon?.toDouble()!!
                                            )
                                        )
                                        polyLines = TMapPolyLine(
                                            "polylines${polylineindex}${i}",
                                            tpointPathList
                                        )




                                        if (selectedRoute.mode == "BUS") {
                                            polyLines.setLineColor(Color.BLUE)
                                        } else {
                                            polyLines.setLineColor(Color.GREEN)
                                        }
                                        polyLines.pathEffect = dashStyle2
                                        tMapView.addTMapPolyLine(polyLines)
                                        polylineindex++
                                        tpointPathList.clear()

                                    }
                                }

                                val info = tMapView.getDisplayTMapInfo(tpointList)
                                tMapView.zoomLevel = info.zoom
                                tMapView.setCenterPoint(info.point.latitude, info.point.longitude)
                            }

                            } catch (e: Exception) {


                            }

                    }.start()
                    for ((index, selectedPlace) in firstList2!!.dayRoute.withIndex()) {
                        val tpoint = selectedPlace.pointdata!!.tpoint
                        //선택된 장소들 표시
                        if (tpoint != null) {
                            val marker = TMapMarkerItem().apply {
                                id = selectedPlace.pointdata.placeName
                                setTMapPoint(TMapPoint(tpoint.latitude, tpoint.longitude))
                                icon = if (index == 0) iconList[0]
                                else if (index == firstList2.dayRoute.size - 1) iconList[11]
                                else iconList[index]
                            }
                            tMapView.addTMapMarkerItem(marker)
                        }
                    }
                }
                else {
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
                    for ((index, selectedPlace) in firstList!!.withIndex()) {
                        val tpoint = selectedPlace.pointdata!!.tpoint
                        //선택된 장소들 표시
                        if (tpoint != null) {
                            val marker = TMapMarkerItem().apply {
                                id = selectedPlace.pointdata.placeName
                                setTMapPoint(TMapPoint(tpoint.latitude, tpoint.longitude))
                                icon = if (index == 0) iconList[0]
                                else if (index == firstList.size - 1) iconList[11]
                                else iconList[index]
                            }
                            tMapView.addTMapMarkerItem(marker)
                        }
                    }
                }





    }})}}





