package com.example.kakaotest

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapPolyLine

class CreatedPath : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.created_path)

      //  SavedUser().getUserDataFromSharedPreferences(this) //회원정보 문서 ID


        // Intent request code
        val REQUEST_ENABLE_BT = 2



        // CreatedPath에서 데이터 받아오기
        val selectedPlacesList =
            intent.getParcelableArrayListExtra<SearchData>("selectedPlacesList")


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
                Toast.makeText(this@CreatedPath, "MapLoading", Toast.LENGTH_SHORT).show()
                //지도 중심좌표로 이동
                tMapView.setCenterPoint(37.566474, 126.985022);
                tMapView.zoomLevel = 10


                val iconList = listOf(
                    BitmapFactory.decodeResource(resources, R.drawable.start),
                    BitmapFactory.decodeResource(resources, R.drawable.pass),
                    BitmapFactory.decodeResource(resources, R.drawable.end),

                )


                for ((index, selectedPlace) in selectedPlacesList!!.withIndex()) {
                    val tpoint = selectedPlace.tpoint

                    //선택된 장소들 표시
                    if (tpoint != null) {
                        val marker = TMapMarkerItem().apply {
                            id = selectedPlace.id
                            setTMapPoint(TMapPoint(tpoint.latitude, tpoint.longitude))
                            icon = if (index==0) iconList[0]
                            else if (index == selectedPlacesList.size-1) iconList[2]
                            else iconList[1]

                        }
                        tMapView.addTMapMarkerItem(marker)
                    }


                }


                // 선택된 장소들의 TMapPoint를 이용하여 리스트 생성
                val pointList = ArrayList<TMapPoint>()
                for (selectedPlace in selectedPlacesList!!) {
                    selectedPlace.tpoint?.let { tMapPoint ->
                        pointList.add(tMapPoint)
                    }
                }



                // 선택된 장소들 간의 경로 표시
                for (i in 0 until pointList.size - 1) {
                    val start = pointList[i]
                    val end = pointList[i + 1]



                    TMapData().findPathData(start, end) { tMapPolyLine ->
                       // tMapPolyLine.lineWidth = 5f
                      //  tMapPolyLine.lineColor = Color.BLUE
                      //  tMapPolyLine.lineAlpha = 255
                      //  tMapPolyLine.outLineWidth = 5f
                      //  tMapPolyLine.outLineColor = Color.RED
                       // tMapPolyLine.outLineAlpha = 255

                        // 새로운 PolyLine을 생성하여 기존 PolyLine의 좌표를 복사
                    //    val newPolyLine = TMapPolyLine()
                        val newPolyLine = TMapPolyLine("line1", pointList)
                          //  newPolyLine.linePointList.addAll(tMapPolyLine.linePointList)

                        // 지도에 추가
                        tMapView.addTMapPolyLine(newPolyLine)

                        // 경로 추가 후 지도의 중심 및 줌 조절
                        val info = tMapView.getDisplayTMapInfo(newPolyLine.linePointList)
                        tMapView.zoomLevel = info.zoom
                        tMapView.setCenterPoint(info.point.latitude, info.point.longitude)
                    }
                }

            }
        })

    }
}

