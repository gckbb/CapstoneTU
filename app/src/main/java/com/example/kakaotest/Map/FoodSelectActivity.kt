package com.example.kakaotest.Map

import DataAdapter
import android.content.Context
import android.content.DialogInterface
import android.content.Intent

import android.graphics.BitmapFactory

import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.tmap.SearchData
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.example.kakaotest.R

import com.example.kakaotest.Utility.Adapter.SelectRecyclerAdapter
import com.example.kakaotest.Utility.SharedPreferenceUtil
import com.example.kakaotest.Utility.dialog.AlertDialogHelper

import com.example.kakaotest.databinding.ActivityFoodSelectBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.poi.TMapPOIItem

class FoodSelectActivity : AppCompatActivity(), DataAdapter.ListBtnClickListener {
    private val selectedFoodPlacesList = java.util.ArrayList<SearchData>() //선택한 장소 저장하는 list
    val searchDataList = arrayListOf<SearchData>()
    var searchDataList2 = java.util.ArrayList<SearchData>()
    private lateinit var adapter: DataAdapter
    private var mBinding: ActivityFoodSelectBinding ?= null

    private lateinit var tMapView: TMapView

    private val binding get() = mBinding!!
    var startpoint: TMapPoint? = null
    var endpoint: TMapPoint? = null
    lateinit var start: TMapPoint

    lateinit var end: TMapPoint

    private lateinit var selectRecyclerAdapter: SelectRecyclerAdapter
    private val bottomSheetLayout by lazy { findViewById<LinearLayout>(R.id.bottom_sheet_layout) }
    private val bottomSheetHidePersistentButton by lazy { findViewById<Button>(R.id.hide_persistent_button) }
    private val selectPlaceList by lazy { bottomSheetLayout.findViewById<RecyclerView>(R.id.selectPlace) }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFoodSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("app_key", "8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP").apply()

        // 값을 가져옴
        val appKey: String? = sharedPreferences.getString("app_key", null)

        // FrameLayout 컨테이너를 XML에서 찾아옴
        val container: FrameLayout = findViewById(R.id.tmapViewContainer)
        // TMapView 인스턴스를 생성

        tMapView = TMapView(this)

        val tMapData = TMapData()
        val tMapGps = TMapPoint()


        // TMapView를 FrameLayout에 추가
        container.addView(tMapView)
        // 발급받은 키로 TMapView에 API 키 설정
        tMapView.setSKTMapApiKey(appKey)



        adapter = DataAdapter(this,R.layout.data_list, searchDataList,this)
        binding.searchDataListView.adapter = adapter




        selectPlaceList.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )
        selectRecyclerAdapter = SelectRecyclerAdapter(
            this,
            selectedFoodPlacesList,
            selectedFoodPlacesList
        ) { position ->
            onItemDelete(position) // 삭제 콜백 설정
        }

        selectPlaceList.adapter = selectRecyclerAdapter

        adapter.selectRecyclerAdapter = selectRecyclerAdapter

        // 어댑터를 초기화한 후
        adapter.setOnDeleteListener { position ->
            adapter.deleteItem(position)
        }



        binding.searchDataListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectItem = parent.getItemAtPosition(position) as SearchData


            tMapView.setCenterPoint(selectItem.tpoint.latitude,selectItem.tpoint.longitude)
            tMapView.zoomLevel = 15


            // 중복된 장소인지 확인
            if (!isPlaceAlreadySelected(selectItem)) {

                tMapView.removeAllTMapMarkerItem()
                tMapView.removeAllTMapPOIItem()
                selectedFoodPlacesList.clear()
                // 선택한 장소를 리스트에 추가
                selectedFoodPlacesList.add(selectItem)


                selectRecyclerAdapter.notifyDataSetChanged() // RecyclerView 어댑터에 데이터 변경 알림
                updateMarkers()
                Log.d("ITEM", selectedFoodPlacesList.toString())

            }
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        // 클릭 이벤트 설정
        tMapView.setOnClickListenerCallback(object : TMapView.OnClickListenerCallback {
            override fun onPressDown( // 터치함
                p0: java.util.ArrayList<TMapMarkerItem>?,
                p1: java.util.ArrayList<TMapPOIItem>?,
                p2: TMapPoint?,
                p3: PointF?
            ) {

            }

            override fun onPressUp( // 떨어짐
                p0: java.util.ArrayList<TMapMarkerItem>?,
                p1: java.util.ArrayList<TMapPOIItem>?,
                p2: TMapPoint?,
                p3: PointF?
            ) {

            }
        })


        tMapView.setOnMapReadyListener(object : TMapView.OnMapReadyListener {
            override fun onMapReady() {
                // 맵 로딩이 완료된 후에 수행할 동작을 구현해주세요
                Toast.makeText(this@FoodSelectActivity, "MapLoading", Toast.LENGTH_SHORT).show()
                tMapView.setCenterPoint(tMapGps.katecLat, tMapGps.katecLon)
                tMapView.zoomLevel = 10
            }
        })



        // Search Button 클릭 리스너 설정
        binding.searchButton.setOnClickListener {
            try {

                tMapView.removeAllTMapMarkerItem()
                searchDataList.clear()
                val strData = binding.searchText.text.toString()
                tMapData.findAllPOI(strData, TMapData.OnFindAllPOIListener { poiItemList ->
                    for (item in poiItemList) {
                        searchDataList.add(SearchData(item.poiName, item.poiPoint, item.poiAddress))
                    }
                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                    }

                    tMapView.addTMapPOIItem(poiItemList)

                })
            }   catch (e: Exception) {
                Log.e("FoodSelectActivity", "searchBtn action - Exception: ${e.toString()}", e)
            }
        }

        binding.nextbutton.setOnClickListener {

            try {

                val intent = Intent(this, RouteListActivity::class.java)
                SharedPreferenceUtil.saveFoodToSharedPreferences(this, selectedFoodPlacesList)

                startActivity(intent)
                Log.d(
                    "selectedFoodPlacesList ",
                    "selectedFoodPlacesList :  " + selectedFoodPlacesList.toString()
                )

            } catch (e: Exception) {
                Log.e("FoodSelectActivity", "intent error - $e")
            }


        }

        initializePersistentBottomSheet()
        persistentBottomSheetEvent()


    }
    override fun onResume() {
        super.onResume()

        binding.showButton.setOnClickListener {
            // BottomSheet의 peek_height만큼 보여주기
            bottomSheetBehavior.peekHeight = 800
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    // Persistent BottomSheet 초기화
    private fun initializePersistentBottomSheet() {

        // BottomSheetBehavior에 layout 설정
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                // BottomSheetBehavior state에 따른 이벤트
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        Log.d("MainActivity", "state: hidden")
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        Log.d("MainActivity", "state: expanded")
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        Log.d("MainActivity", "state: collapsed")
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        Log.d("MainActivity", "state: dragging")
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        Log.d("MainActivity", "state: settling")
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        Log.d("MainActivity", "state: half expanded")
                    }
                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })

    }

    // PersistentBottomSheet 내부 버튼 click event
    private fun persistentBottomSheetEvent() {



        bottomSheetHidePersistentButton.setOnClickListener {
            // BottomSheet 숨김
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        }

    }

    private fun updateMarkers() {
        tMapView.removeAllTMapPOIItem()
        tMapView.removeAllTMapMarkerItem() // 기존 마커 제거

        selectRecyclerAdapter.notifyDataSetChanged()
        for (place in selectedFoodPlacesList) {
            val marker = TMapMarkerItem() // 마커 객체를 초기화합니다.


            marker.id = place.id
            marker.tMapPoint = TMapPoint(place.tpoint.latitude, place.tpoint.longitude)
            marker.icon = BitmapFactory.decodeResource(resources, R.drawable.point)
            marker.setPosition(0.5f, 1.0f) // 마커 아이콘의 중심점을 설정합니다.
            tMapView.addTMapMarkerItem(marker)
            // 마커가 제대로 설정되었는지 로그로 확인
            Log.d("MapActivity", "Marker added: ${place.id}, ${place.tpoint.latitude}, ${place.tpoint.longitude}")

            //     tMapView.addTMapMarkerItem(marker)
            tMapView.setCenterPoint(place.tpoint.latitude,place.tpoint.latitude)



        }



        if (selectedFoodPlacesList.isNotEmpty()) {
            val centerPlace = selectedFoodPlacesList[0]
            tMapView.setCenterPoint(centerPlace.tpoint.latitude, centerPlace.tpoint.longitude)
        }






    }

    override fun onItemClick(item: SearchData) {
        if (! selectedFoodPlacesList.contains(item)) {
            selectedFoodPlacesList.add(item)


        } else {
            selectedFoodPlacesList.remove(item)

        }
        updateMarkers()
        selectRecyclerAdapter.notifyDataSetChanged() // RecyclerView 어댑터에 데이터 변경 알림
    }
    override fun onListBtnClick(position: Int, updatedSelectedPlacesList: ArrayList<SearchData>) {
        tMapView.removeAllTMapMarkerItem()
        tMapView.removeAllTMapPOIItem()
        selectedFoodPlacesList.clear()
        selectedFoodPlacesList.addAll(updatedSelectedPlacesList)

        updateMarkers()
        selectRecyclerAdapter.notifyDataSetChanged() // RecyclerView 어댑터에 데이터 변경 알림
        Log.d("ClickedItems", selectedFoodPlacesList.toString())
    }

    fun onItemDelete(position: Int) {
        selectedFoodPlacesList.removeAt(position)
        //   selectRecyclerAdapter.removeItem(selectPlaceList.get(position))
        updateMarkers()

    }
    private fun isPlaceAlreadySelected(newPlace: SearchData): Boolean {
        // 중복된 장소인지 확인
        for (selectedPlace in selectedFoodPlacesList) {
            if (selectedPlace.id == newPlace.id) {
                return true // 이미 선택된 장소가 존재하면 중복으로 판단
            }
        }
        return false // 선택된 장소가 없으면 중복이 아님
    }}