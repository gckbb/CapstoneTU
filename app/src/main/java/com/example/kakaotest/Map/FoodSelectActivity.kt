package com.example.kakaotest.Map

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.tmap.FeatureCollection
import com.example.kakaotest.DataModel.tmap.SearchData
import com.example.kakaotest.Plan.SelectedPlaceData
import com.example.kakaotest.R
import com.example.kakaotest.Utility.Adapter.DataAdapter
import com.example.kakaotest.Utility.tmap.ApiService
import com.example.kakaotest.databinding.ActivityFoodMapBinding
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


class FoodSelectActivity : AppCompatActivity() {
    private val selectedFoodPlacesList = ArrayList<SearchData>() //선택한 장소 저장하는 list
    val searchDataList = arrayListOf<SearchData>()
    var searchDataList2 = ArrayList<SearchData>()

    private var mBinding: ActivityFoodMapBinding ?= null
    private val binding get() = mBinding!!
    var startpoint: TMapPoint? = null
    var endpoint: TMapPoint? = null
    lateinit var start: TMapPoint
    lateinit var end: TMapPoint
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFoodMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val receivedDataList = intent.getParcelableArrayListExtra<SelectedPlaceData>("selectedPlaceDataList")

        //이부분 tmap sdk에도 BuildConfig가 있어서 고생좀 함
        //여기 오류나면 상단바 Build -> Rebuild Project 누르면 됨
        //   val appKey: String = BuildConfig.app_key

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("app_key", "8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP").apply()

        // 값을 가져옴
        val appKey: String? = sharedPreferences.getString("app_key", null)

        // FrameLayout 컨테이너를 XML에서 찾아옴
        val container: FrameLayout = findViewById(R.id.tmapViewContainer)
        // TMapView 인스턴스를 생성
        val tMapView = TMapView(this@FoodSelectActivity)
        val tMapData = TMapData()
        val tMapGps = TMapPoint()


        // TMapView를 FrameLayout에 추가
        container.addView(tMapView)
        // 발급받은 키로 TMapView에 API 키 설정
        tMapView.setSKTMapApiKey(appKey)

        val searchDataAdapter = DataAdapter(this,searchDataList)
        binding.searchDataListView.adapter = searchDataAdapter



        binding.searchDataListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectItem = parent.getItemAtPosition(position) as SearchData

            //apiAdapter.apiRequest(126.862833,35.151286,126.883917,35.153113)
            // Toast.makeText(this, "${selectItem.tpoint.latitude}", Toast.LENGTH_SHORT).show()
            tMapView.setCenterPoint(selectItem.tpoint.latitude,selectItem.tpoint.longitude)
            tMapView.zoomLevel = 15


            if(startpoint==null || endpoint==null){
                if(startpoint==null){
                    startpoint = selectItem.tpoint
                    //  Toast.makeText(this, "시작지역: ${selectItem.id}", Toast.LENGTH_SHORT).show()
                }else if(endpoint==null){
                    endpoint = selectItem.tpoint
                    // Toast.makeText(this, "도착지역: ${selectItem.id}", Toast.LENGTH_SHORT).show()
                }
            }

            // 중복된 장소인지 확인
            if (!isPlaceAlreadySelected(selectItem)) {
                //  Toast.makeText(this, selectItem.id, Toast.LENGTH_SHORT).show()

                // 선택한 장소를 리스트에 추가
                selectedFoodPlacesList.add(selectItem)



                // 선택한 장소를 SelectedPlaceData에 추가
                val selectedPlaceData = SelectedPlaceData(
                    placeName = selectItem.id,
                    tpoint = TMapPoint(selectItem.tpoint.latitude, selectItem.tpoint.longitude),
                    address = selectItem.address

                )

                Log.d("ITEM", selectItem.toString())

                // 리스트에 추가된 모든 장소를 SelectedPlaceData 리스트로 변환
                val selectedPlaceDataList = selectedFoodPlacesList.map {
                    SelectedPlaceData(
                        placeName = it.id,
                        tpoint = TMapPoint(it.tpoint.latitude, it.tpoint.longitude),
                        address = it.address
                    )

                }



                binding.nextbuttons.setOnClickListener {
                    val intent = Intent(this, RouteListActivity::class.java)
                    val travelPlan = intent.getParcelableExtra<TravelPlan>("travelPlan")
                    intent.putExtra("travelPlan", travelPlan)
                    intent.putParcelableArrayListExtra("selectedPlaceDataList", receivedDataList)
                    intent.putParcelableArrayListExtra("selectedFoodDataList", ArrayList(selectedPlaceDataList))
                    startActivity(intent)
                    Log.d("Item", selectedFoodPlacesList.toString())

                }

            } else {
                Toast.makeText(this, "이미 선택된 장소입니다.", Toast.LENGTH_SHORT).show()
            }

        }

        // 클릭 이벤트 설정
        tMapView.setOnClickListenerCallback(object : OnClickListenerCallback {
            override fun onPressDown( // 터치함
                p0: ArrayList<TMapMarkerItem>?,
                p1: ArrayList<TMapPOIItem>?,
                p2: TMapPoint?,
                p3: PointF?
            ) {
                //Toast.makeText(this@FoodSelectActivity, "onPressDown", Toast.LENGTH_SHORT).show()
            }

            override fun onPressUp( // 떨어짐
                p0: ArrayList<TMapMarkerItem>?,
                p1: ArrayList<TMapPOIItem>?,
                p2: TMapPoint?,
                p3: PointF?
            ) {
                //Toast.makeText(this@FoodSelectActivity, "onPressUp", Toast.LENGTH_SHORT).show()
            }
        })





        // 맵 로딩 완료 시 동작할 리스너 설정
        tMapView.setOnMapReadyListener(object : TMapView.OnMapReadyListener {
            override fun onMapReady() {
                // 맵 로딩이 완료된 후에 수행할 동작을 구현해주세요
                // 예: 마커 추가, 경로 표시 등
                Toast.makeText(this@FoodSelectActivity, "MapLoading", Toast.LENGTH_SHORT).show()
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
                                marker.icon = BitmapFactory.decodeResource(resources,
                                    R.drawable.poi
                                )
                                tMapView.addTMapMarkerItem(marker)
                                searchDataList2.add(SearchData(item.poiName,item.poiPoint,item.poiAddress))
                                runOnUiThread{
                                    searchDataList.add(SearchData(item.poiName,item.poiPoint,item.poiAddress))
                                    searchDataAdapter.notifyDataSetChanged()
                                }
                                num += 1
                            }

                            /*
                                                        routetest.routeSet(searchDataList2,searchDataList2[0])
                                                        routetest.routeStart(2,6)
                                                        routetest.printTotalRoute()
                            */
                            tMapView.addTMapPOIItem(poiItemList)
                        })

                }




            }
        })








    }



    private fun isPlaceAlreadySelected(newPlace: SearchData): Boolean {
        // 중복된 장소인지 확인
        for (selectedPlace in selectedFoodPlacesList) {
            if (selectedPlace.id == newPlace.id) {
                return true // 이미 선택된 장소가 존재하면 중복으로 판단
            }
        }
        return false // 선택된 장소가 없으면 중복이 아님
    }
    fun apiRequest(startX: Number, startY: Number, endX: Number, endY: Number): Number? {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://apis.openapi.sk.com/tmap/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService: ApiService = retrofit.create(ApiService::class.java)

        var input = HashMap<String, Any>()
        input["startY"] = startY
        input["startX"] = startX
        input["endY"] = endY
        input["endX"] = endX
        input["totalValue"] = 2

        val routeCall: Call<FeatureCollection> = apiService.getRoute(input)
        var totalTime: Number? = null

        routeCall.enqueue(object : Callback<FeatureCollection> {
            override fun onResponse(
                call: Call<FeatureCollection?>,
                response: Response<FeatureCollection?>
            ) {
                val RouteInfo: FeatureCollection? = response.body()
                Log.d(ContentValues.TAG, "getPostList onResponse()")
                Log.d(ContentValues.TAG, "type : ${RouteInfo?.features?.get(0)?.type}")
                Log.d(
                    ContentValues.TAG,
                    "TotalTime : ${RouteInfo?.features?.get(0)?.properties?.totalTime}"
                )
                Log.d(
                    ContentValues.TAG,
                    "TotalDistance : ${RouteInfo?.features?.get(0)?.properties?.totalDistance}"
                )
                Log.d(
                    ContentValues.TAG,
                    "TotalFare : ${RouteInfo?.features?.get(0)?.properties?.totalFare}"
                )
                totalTime = RouteInfo?.features?.get(0)?.properties?.totalTime
            }

            override fun onFailure(call: Call<FeatureCollection?>, t: Throwable) {
                call.cancel()
                Log.d(ContentValues.TAG, "api fail")
                totalTime = null
            }

        })
        return totalTime
    }
}