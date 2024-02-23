package com.example.kakaotest

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.databinding.ActivityMapBinding
import com.skt.tmap.TMapData
import com.skt.tmap.TMapData.OnFindAllPOIListener
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.TMapView.OnClickListenerCallback
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.poi.TMapPOIItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import java.util.ArrayList
import java.util.HashMap


class MapActivity : AppCompatActivity() {

    val searchDataList = arrayListOf<SearchData>()
    var searchDataList2 = ArrayList<SearchData>()

    private var mBinding: ActivityMapBinding ?= null
    private val binding get() = mBinding!!
    private val routetest = MakeRoute()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //이부분 tmap sdk에도 BuildConfig가 있어서 고생좀 함
        //여기 오류나면 상단바 Build -> Rebuild Project 누르면 됨
        val appKey: String = BuildConfig.app_key

        // FrameLayout 컨테이너를 XML에서 찾아옴
        val container: FrameLayout = findViewById(R.id.tmapViewContainer)
        // TMapView 인스턴스를 생성
        val tMapView = TMapView(this@MapActivity)
        val tMapData = TMapData()
        val tMapGps = TMapPoint()
        val apiAdapter = ApiAdapter()

        // TMapView를 FrameLayout에 추가
        container.addView(tMapView)
        // 발급받은 키로 TMapView에 API 키 설정
        tMapView.setSKTMapApiKey(appKey)

        val searchDataAdapter = DataAdapter(this,searchDataList)
        binding.searchDataListView.adapter = searchDataAdapter



        binding.searchDataListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectItem = parent.getItemAtPosition(position) as SearchData

            //apiAdapter.apiRequest(126.862833,35.151286,126.883917,35.153113)
            Toast.makeText(this, "${selectItem.tpoint.latitude}", Toast.LENGTH_SHORT).show()
            tMapView.setCenterPoint(selectItem.tpoint.latitude,selectItem.tpoint.longitude)
            tMapView.zoomLevel = 15

        }

        // 클릭 이벤트 설정
        tMapView.setOnClickListenerCallback(object : OnClickListenerCallback {
            override fun onPressDown( // 터치함
                p0: ArrayList<TMapMarkerItem>?,
                p1: ArrayList<TMapPOIItem>?,
                p2: TMapPoint?,
                p3: PointF?
            ) {
                Toast.makeText(this@MapActivity, "onPressDown", Toast.LENGTH_SHORT).show()
            }

            override fun onPressUp( // 떨어짐
                p0: ArrayList<TMapMarkerItem>?,
                p1: ArrayList<TMapPOIItem>?,
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
                tMapView.setCenterPoint(tMapGps.katecLat, tMapGps.katecLon)
                tMapView.zoomLevel = 10

                val marker = TMapMarkerItem()
                marker.id = "marker1"
                marker.setTMapPoint(tMapGps.katecLat, tMapGps.katecLon)
                marker.icon = BitmapFactory.decodeResource(resources, R.drawable.point)
                tMapView.addTMapMarkerItem(marker)


                binding.searchButton.setOnClickListener{
                    tMapView.removeAllTMapMarkerItem()
                    tMapView.removeAllTMapPOIItem()
                    searchDataList.clear()
                    var strData = binding.searchText.text.toString()
                    tMapData.findAllPOI(strData,
                        OnFindAllPOIListener { poiItemList ->
                            var num = 1
                            for (item in poiItemList) {
                                Log.e(
                                    "Poi Item",
                                    "name:" + item.poiName + " address:" + item.poiAddress
                                )
                                marker.id = item.poiName
                                marker.setTMapPoint(TMapPoint())
                                marker.icon = BitmapFactory.decodeResource(resources, R.drawable.poi)
                                tMapView.addTMapMarkerItem(marker)
                                searchDataList2.add(SearchData(item.poiName,item.poiPoint,item.poiAddress))
                                runOnUiThread{
                                    searchDataList.add(SearchData(item.poiName,item.poiPoint,item.poiAddress))
                                    searchDataAdapter.notifyDataSetChanged()
                                }
                                num += 1
                            }

                            routetest.routeSet(searchDataList2,searchDataList2[0])
                            routetest.routeStart(2,6)
                            routetest.printTotalRoute()

                            tMapView.addTMapPOIItem(poiItemList)
                        })

                }
            }
        })


    }
    fun apiRequest(startX :Number,startY :Number,endX :Number,endY :Number) : Number? {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://apis.openapi.sk.com/tmap/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService: ApiService = retrofit.create(ApiService::class.java)

        var input = HashMap<String,Any>()
        input["startY"] = startY
        input["startX"] = startX
        input["endY"] = endY
        input["endX"] = endX
        input["totalValue"] = 2

        val routeCall = apiService.getRoute(input)
        var totalTime :Number? = null

        routeCall.enqueue(object: Callback<FeatureCollection> {
            override fun onResponse(call: Call<FeatureCollection>, response: Response<FeatureCollection>) {
                val RouteInfo : FeatureCollection? = response.body()
                Log.d(ContentValues.TAG, "getPostList onResponse()")
                Log.d(ContentValues.TAG, "type : ${RouteInfo?.features?.get(0)?.type}")
                Log.d(ContentValues.TAG, "TotalTime : ${RouteInfo?.features?.get(0)?.properties?.totalTime}")
                Log.d(ContentValues.TAG, "TotalDistance : ${RouteInfo?.features?.get(0)?.properties?.totalDistance}")
                Log.d(ContentValues.TAG, "TotalFare : ${RouteInfo?.features?.get(0)?.properties?.totalFare}")
                totalTime = RouteInfo?.features?.get(0)?.properties?.totalTime
            }
            override fun onFailure(call: Call<FeatureCollection>, t: Throwable) {
                call.cancel()
                Log.d(ContentValues.TAG, "api fail")
                totalTime = null
            }

        })
        return totalTime
    }
}