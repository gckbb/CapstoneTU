package com.example.kakaotest.Map

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.tmap.SearchData
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.Utility.tmap.ApiAdapter
import com.example.kakaotest.Utility.tmap.MakeRoute
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.example.kakaotest.R
import com.example.kakaotest.Utility.Adapter.DataAdapter
import com.example.kakaotest.Utility.dialog.AlertDialogHelper
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.example.kakaotest.R
import com.example.kakaotest.Utility.Adapter.DataAdapter
import com.example.kakaotest.Utility.tmap.ApiAdapter
import com.example.kakaotest.Utility.tmap.MakeRoute
import com.example.kakaotest.databinding.ActivityMapBinding
import com.skt.tmap.TMapData
import com.skt.tmap.TMapData.OnFindAllPOIListener
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.TMapView.OnClickListenerCallback
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapPolyLine
import com.skt.tmap.poi.TMapPOIItem
import java.util.ArrayList


class MapActivity : AppCompatActivity() {
    private val selectedPlacesList = ArrayList<SearchData>() //선택한 장소 저장하는 list
    val searchDataList = arrayListOf<SearchData>()
    var searchDataList2 = ArrayList<SearchData>()
    var isFirstPlaceSelected = false
    private var mBinding: ActivityMapBinding? = null
    private val binding get() = mBinding!!
    private val routetest = MakeRoute()
    var startpoint: TMapPoint? = null
    var endpoint: TMapPoint? = null
    lateinit var start: TMapPoint
    lateinit var end: TMapPoint
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val travelPlan = intent.getParcelableExtra<TravelPlan>("travelPlan")



        binding.backBtn.setOnClickListener {
            finish()

        }


        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("app_key", "8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP")
            .apply()

        // 값을 가져옴
        val appKey: String? = sharedPreferences.getString("app_key", null)

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

        val searchDataAdapter = DataAdapter(this, searchDataList)
        binding.searchDataListView.adapter = searchDataAdapter


/*
        if( ){
            AlertDialogHelper().showAlertMessage(this,"숙소를 선택해주세요!","네",null,null,
                DialogInterface.OnClickListener { dialog, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        dialog.dismiss()
                    }})
        } else if (selectedPlacesList[0] != null && selectedPlacesList[1] == null){
            AlertDialogHelper().showAlertMessage(this,"가고싶은 장소를 선택해주세요! \n (식당 제외)","네",null,null,
                DialogInterface.OnClickListener { dialog, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        dialog.dismiss()
                    }})
        }

*/

        binding.searchDataListView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectItem = parent.getItemAtPosition(position) as SearchData

                //apiAdapter.apiRequest(126.862833,35.151286,126.883917,35.153113)
                // Toast.makeText(this, "${selectItem.tpoint.latitude}", Toast.LENGTH_SHORT).show()
                tMapView.setCenterPoint(selectItem.tpoint.latitude, selectItem.tpoint.longitude)
                tMapView.zoomLevel = 15


                if (startpoint == null || endpoint == null) {
                    if (startpoint == null) {
                        startpoint = selectItem.tpoint
                        //  Toast.makeText(this, "시작지역: ${selectItem.id}", Toast.LENGTH_SHORT).show()
                    } else if (endpoint == null) {
                        endpoint = selectItem.tpoint
                        // Toast.makeText(this, "도착지역: ${selectItem.id}", Toast.LENGTH_SHORT).show()
                    }
                }




                // 중복된 장소인지 확인
                if (!isPlaceAlreadySelected(selectItem)) {
                    //  Toast.makeText(this, selectItem.id, Toast.LENGTH_SHORT).show()

                    // 선택한 장소를 리스트에 추가
                    selectedPlacesList.add(selectItem)


                    // 선택한 장소를 SelectedPlaceData에 추가
                    val selectedPlaceData = SelectedPlaceData(
                        placeName = selectItem.id,
                        tpoint = TMapPoint(selectItem.tpoint.latitude, selectItem.tpoint.longitude),
                        address = selectItem.address

                    )

                    Log.d("ITEM", selectItem.toString())

                    // 리스트에 추가된 모든 장소를 SelectedPlaceData 리스트로 변환
                    val selectedPlaceDataList = selectedPlacesList.map {
                        SelectedPlaceData(
                            placeName = it.id,
                            tpoint = TMapPoint(it.tpoint.latitude, it.tpoint.longitude),
                            address = it.address
                        )

                    }



                    binding.nextbutton.setOnClickListener {
                        val intent = Intent(this, SelectedPlace::class.java)

                        intent.putParcelableArrayListExtra(
                            "selectedPlaceDataList",
                            ArrayList(selectedPlaceDataList)
                        )
                        intent.putExtra("travelPlan", travelPlan)
                        startActivity(intent)
                        Log.d("Item", selectedPlacesList.toString())

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


        var editText = findViewById<EditText>(R.id.searchText)
        editText.setOnEditorActionListener { textView, action, keyEvent ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_DONE) {
                Log.d("성공",editText.text.toString())
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
                handled = true
            }

            handled
        }


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



                binding.searchButton.setOnClickListener {
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
                                marker.icon = BitmapFactory.decodeResource(
                                    resources,
                                    R.drawable.poi
                                )
                                tMapView.addTMapMarkerItem(marker)
                                searchDataList2.add(
                                    SearchData(
                                        item.poiName,
                                        item.poiPoint,
                                        item.poiAddress
                                    )
                                )
                                runOnUiThread {
                                    searchDataList.add(
                                        SearchData(
                                            item.poiName,
                                            item.poiPoint,
                                            item.poiAddress
                                        )
                                    )
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



})}


    private fun isPlaceAlreadySelected(newPlace: SearchData): Boolean{
        // 중복된 장소인지 확인
        for (selectedPlace in selectedPlacesList) {
            if (selectedPlace.id == newPlace.id) {
                return true // 이미 선택된 장소가 존재하면 중복으로 판단
            }
        }
        return false // 선택된 장소가 없으면 중복이 아님
    }

}