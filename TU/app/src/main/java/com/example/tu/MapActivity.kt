package com.example.tu

import android.graphics.BitmapFactory
import android.graphics.PointF
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.TMapView.OnClickListenerCallback
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.poi.TMapPOIItem


class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        //이부분 tmap sdk에도 BuildConfig가 있어서 고생좀 함
        val appKey: String = com.example.tu.BuildConfig.app_key

        // FrameLayout 컨테이너를 XML에서 찾아옴
        val container: FrameLayout = findViewById(R.id.tmapViewContainer)
        // TMapView 인스턴스를 생성
        val tMapView = TMapView(this@MapActivity)
        // TMapView를 FrameLayout에 추가
        container.addView(tMapView)
        // 발급받은 키로 TMapView에 API 키 설정
        tMapView.setSKTMapApiKey(appKey)

        // 클릭 이벤트 설정
        tMapView.setOnClickListenerCallback(object : OnClickListenerCallback {
            override fun onPressDown( // 터치함
                p0: java.util.ArrayList<TMapMarkerItem>?,
                p1: java.util.ArrayList<TMapPOIItem>?,
                p2: TMapPoint?,
                p3: PointF?
            ) {
                Toast.makeText(this@MapActivity, "onPressDown", Toast.LENGTH_SHORT).show()
            }

            override fun onPressUp( // 떨어짐
                p0: java.util.ArrayList<TMapMarkerItem>?,
                p1: java.util.ArrayList<TMapPOIItem>?,
                p2: TMapPoint?,
                p3: PointF?
            ) {
                Toast.makeText(this@MapActivity, "onPressUp", Toast.LENGTH_SHORT).show()
            }
        })

        // 맵 로딩 완료 시 동작할 리스너 설정
        tMapView.setOnMapReadyListener(object : TMapView.OnMapReadyListener {
            override fun onMapReady() {
                // 맵 로딩이 완료된 후에 수행할 동작을 구현해주세요
                // 예: 마커 추가, 경로 표시 등
                Toast.makeText(this@MapActivity, "MapLoading", Toast.LENGTH_SHORT).show()
                tMapView.setCenterPoint(37.468954, 126.4544153)
                tMapView.zoomLevel = 10

                val marker = TMapMarkerItem()
                marker.id = "marker1"
                marker.setTMapPoint(37.468954, 126.4544153)
                marker.icon = BitmapFactory.decodeResource(resources, R.drawable.poi_dot)
                tMapView.addTMapMarkerItem(marker)

            }
        })


    }

}