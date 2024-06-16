package com.example.kakaotest.Map

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.metaRoute.MetaDayRoute
import com.example.kakaotest.R
import com.google.gson.Gson
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapPolyLine


class SingleMetaRoute : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_meta_route)

        val gson = Gson()
        lateinit var dayList:MetaDayRoute
        val travelPlan = intent.getParcelableExtra<TravelPlan>("travelPlan")
        intent.getStringExtra("dayList")?.let { dayListdata2 ->
            dayList = gson.fromJson(dayListdata2, MetaDayRoute::class.java)
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
                Toast.makeText(this@SingleMetaRoute, "MapLoading", Toast.LENGTH_SHORT).show()
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
                    BitmapFactory.decodeResource(resources, R.drawable.end),
                    BitmapFactory.decodeResource(resources, R.drawable.busmarker),
                    BitmapFactory.decodeResource(resources, R.drawable.subwaymarker),
                    BitmapFactory.decodeResource(resources, R.drawable.walkmarker)

                )
                tMapView.removeAllTMapPolyLine()
                tMapView.removeAllTMapMarkerItem()
                for(i in timeindex until timeindex+2) {
                    val tpoint = dayList?.dayRoute?.get(i)?.pointdata

                    if(tpoint != null) {
                        val marker = TMapMarkerItem().apply {
                            id = tpoint.placeName
                            setTMapPoint(TMapPoint(tpoint.tpoint.latitude, tpoint.tpoint.longitude))
                            icon = iconList[i]
                        }
                        tMapView.addTMapMarkerItem(marker)
                    }
                }




                Thread {
                    try {
                        var polylineindex = 1
                        var polyLines: TMapPolyLine
                        var tpointList: ArrayList<TMapPoint> = ArrayList<TMapPoint>()
                        var tpointPathList: ArrayList<TMapPoint> = ArrayList<TMapPoint>()
                        tpointList.add(TMapPoint(dayList.dayRoute?.get(timeindex+1)?.metaData?.requestParameters?.startX?.toDouble()!!,dayList.dayRoute?.get(timeindex+1)?.metaData?.requestParameters?.startY?.toDouble()!!))
                        var dashStyle: IntArray = IntArray(2)  // 점선
                        dashStyle[0] = 10
                        dashStyle[1] = 20
                        var dashStyle2: IntArray = IntArray(2) // 실선
                        dashStyle.plus(0)
                        dashStyle.plus(0)
                        for(selectedRoute in dayList?.dayRoute?.get(timeindex+1)?.metaData?.plan?.itineraries?.get(0)?.legs!!){
                            if(selectedRoute.mode == "WALK") {
                                val marker = TMapMarkerItem().apply {
                                    id = "indeex${polylineindex}"
                                    setTMapPoint(TMapPoint(selectedRoute.start?.lat?.toDouble()!!,selectedRoute.start?.lon?.toDouble()!!))
                                    icon = iconList[14]
                                }
                                tMapView.addTMapMarkerItem(marker)

                                tpointList.add(TMapPoint(selectedRoute.end?.lat?.toDouble()!!,selectedRoute.end?.lon?.toDouble()!!))
                                polyLines = tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH,
                                    TMapPoint(selectedRoute.start?.lat?.toDouble()!!,selectedRoute.start?.lon?.toDouble()!!),
                                    TMapPoint(selectedRoute.end?.lat?.toDouble()!!,selectedRoute.end?.lon?.toDouble()!!))
                                polyLines.setID("polylin${polylineindex}")
                                polyLines.pathEffect = dashStyle
                                polyLines.setLineColor(Color.BLACK)
                                tMapView.addTMapPolyLine(polyLines)
                                polylineindex++
                            }
                            else if(selectedRoute.mode == "BUS" || selectedRoute.mode == "SUBWAY") {
                                val marker = TMapMarkerItem().apply {
                                    id = "index${polylineindex}"
                                    setTMapPoint(TMapPoint(selectedRoute.start?.lat?.toDouble()!!,selectedRoute.start?.lon?.toDouble()!!))
                                    icon = if(selectedRoute.mode == "BUS") iconList[12]
                                    else iconList[13]
                                }
                                tMapView.addTMapMarkerItem(marker)
                                for(i in 1 until selectedRoute.passStopList?.stationList?.size!!) {
                                    tpointList?.add(TMapPoint(selectedRoute.passStopList?.stationList.get(i).lat.toDouble(),selectedRoute.passStopList?.stationList.get(i).lon.toDouble()))

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


                                 /*       tpointPathList.add(
                                            TMapPoint(
                                                selectedRoute.start?.lat?.toDouble()!!,
                                                selectedRoute.start?.lon?.toDouble()!!
                                            )
                                        )
                                        val split =
                                            selectedRoute.passShape?.lineString?.split(",", " ")
                                        for (i in 0 until split?.size!! / 2) {
                                            tpointPathList.add(
                                                TMapPoint(
                                                    split.get(i * 2).toDouble(),
                                                    split.get((i * 2) + 1).toDouble()
                                                )
                                            )
                                        }
                                        tpointPathList.add(
                                            TMapPoint(
                                                selectedRoute.end?.lat?.toDouble()!!,
                                                selectedRoute.end?.lon?.toDouble()!!
                                            )
                                        )
                                        polyLines = TMapPolyLine("polylines${polylineindex}",tpointPathList)

                                  */


                                    if(selectedRoute.mode == "BUS") polyLines.setLineColor(Color.BLUE)
                                    else polyLines.setLineColor(Color.GREEN)
                                    polyLines.pathEffect = dashStyle2
                                    tMapView.addTMapPolyLine(polyLines)
                                    polylineindex++
                                }

                            }
                        }

                        val info = tMapView.getDisplayTMapInfo(tpointList)
                        tMapView.zoomLevel = info.zoom
                        tMapView.setCenterPoint(info.point.latitude, info.point.longitude)

                    } catch (e: Exception) {


                    }
                }.start()
            }})}}





