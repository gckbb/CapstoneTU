package com.example.kakaotest.Map

import DataAdapter
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.tmap.SearchData
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.example.kakaotest.R
import com.example.kakaotest.Utility.Adapter.SelectRecyclerAdapter
<<<<<<< HEAD
import com.example.kakaotest.Utility.SharedPreferenceUtil
=======
>>>>>>> other-origin/K_Ho_demo2
import com.example.kakaotest.databinding.ActivityMapBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.poi.TMapPOIItem
import java.util.ArrayList

class MapActivity : AppCompatActivity(), DataAdapter.ListBtnClickListener {
    private val selectedPlacesList = ArrayList<SearchData>() //선택한 장소 저장하는 list
    val searchDataList = arrayListOf<SearchData>()
    var searchDataList2 = ArrayList<SearchData>()
    private var mBinding: ActivityMapBinding? = null
    private lateinit var selectRecyclerAdapter: SelectRecyclerAdapter
    private lateinit var tMapView: TMapView
    private lateinit var dataadapter: DataAdapter




    private val bottomSheetLayout by lazy { findViewById<LinearLayout>(R.id.bottom_sheet_layout) }

    private val bottomSheetHidePersistentButton by lazy { findViewById<Button>(R.id.hide_persistent_button) }
    private val selectPlaceList by lazy { bottomSheetLayout.findViewById<RecyclerView>(R.id.selectPlace) }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.backBtn.setOnClickListener {
            finish()
        }

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("app_key", "8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP").apply()

        // 값을 가져옴
        val appKey: String? = sharedPreferences.getString("app_key", null)

        // FrameLayout 컨테이너를 XML에서 찾아옴
        val container: FrameLayout = findViewById(R.id.tmapViewContainer)

        // TMapView 인스턴스를 생성
        tMapView = TMapView(this@MapActivity)
       tMapView.setSKTMapApiKey ("app_key")
        val tMapData = TMapData()
        val tMapGps = TMapPoint()








        // TMapView를 FrameLayout에 추가
        container.addView(tMapView)
        // 발급받은 키로 TMapView에 API 키 설정
        tMapView.setSKTMapApiKey(appKey)


        dataadapter = DataAdapter(this, R.layout.data_list, searchDataList, this)

        binding.searchDataListView.adapter = dataadapter



        selectPlaceList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        selectRecyclerAdapter = SelectRecyclerAdapter(this,selectedPlacesList,selectedPlacesList){ position ->
            onItemDelete(position) // 삭제 콜백 설정
        }
        selectPlaceList.adapter = selectRecyclerAdapter

        dataadapter.selectRecyclerAdapter = selectRecyclerAdapter


        // 어댑터를 초기화한 후
        dataadapter.setOnDeleteListener { position ->
            dataadapter.deleteItem(position)
        }



        binding.searchDataListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectItem = parent.getItemAtPosition(position) as SearchData
            tMapView.setCenterPoint(selectItem.tpoint.longitude, selectItem.tpoint.latitude)
            tMapView.zoomLevel = 15

            // 선택한 장소를 리스트에 추가
            if (!isPlaceAlreadySelected(selectItem)) {
                tMapView.removeAllTMapMarkerItem()
                tMapView.removeAllTMapPOIItem()

                selectedPlacesList.clear() // 기존 선택된 장소 리스트를 초기화
                selectedPlacesList.add(selectItem)

                selectRecyclerAdapter.notifyDataSetChanged() // RecyclerView 어댑터에 데이터 변경 알림
                updateMarkers()
            }
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }





        // 클릭 이벤트 설정
        tMapView.setOnClickListenerCallback(object : TMapView.OnClickListenerCallback {
            override fun onPressDown(
                p0: ArrayList<TMapMarkerItem>?,
                p1: ArrayList<TMapPOIItem>?,
                p2: TMapPoint?,
                p3: PointF?
            ) {
                Toast.makeText(this@MapActivity, "onPressDown", Toast.LENGTH_SHORT).show()
            }

            override fun onPressUp(
                p0: ArrayList<TMapMarkerItem>?,
                p1: ArrayList<TMapPOIItem>?,
                p2: TMapPoint?,
                p3: PointF?
            ) {
                Toast.makeText(this@MapActivity, "onPressUp", Toast.LENGTH_SHORT).show()
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
                        dataadapter.notifyDataSetChanged()
                    }
                    tMapView.addTMapPOIItem(poiItemList)
                })
            } catch (e: Exception) {
                Log.e("MapActivity", "searchBtn action - Exception: ${e.toString()}", e)
            }
        }

        binding.nextbutton.setOnClickListener {
            val intent = Intent(this, SelectedPlace::class.java)

            val selectedPlaceDataList = selectedPlacesList.map {
                SelectedPlaceData(
                    placeName = it.id,
                    tpoint = TMapPoint(it.tpoint.latitude, it.tpoint.longitude),
                    address = it.address
                )
            } as ArrayList<SelectedPlaceData>



            SharedPreferenceUtil.saveDataToSharedPreferences(this,selectedPlaceDataList)

        //    intent.putParcelableArrayListExtra("selectedPlaceDataList", selectedPlaceDataList)
          //  intent.putExtra("travelPlan", travelPlan)

            startActivity(intent)
            Log.d("Item", selectedPlaceDataList.toString())
        }

        tMapView.setOnMapReadyListener(object : TMapView.OnMapReadyListener {
            override fun onMapReady() {
                // 맵 로딩이 완료된 후에 수행할 동작을 구현해주세요
                Toast.makeText(this@MapActivity, "MapLoading", Toast.LENGTH_SHORT).show()
                tMapView.setCenterPoint(tMapGps.katecLat, tMapGps.katecLon)
                tMapView.zoomLevel = 10
            }
        })





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
        for (place in selectedPlacesList) {
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


        if (selectedPlacesList.isNotEmpty()) {
            val centerPlace = selectedPlacesList[0]
            tMapView.setCenterPoint(centerPlace.tpoint.latitude, centerPlace.tpoint.longitude)
        }






    }


    private fun isPlaceAlreadySelected(newPlace: SearchData): Boolean {
        for (selectedPlace in selectedPlacesList) {
            if (selectedPlace.id == newPlace.id) {
                return true // 이미 선택된 장소가 존재하면 중복으로 판단
            }
        }
        return false // 선택된 장소가 없으면 중복이 아님
    }


    override fun onItemClick(item: SearchData) {

        if (!selectedPlacesList.contains(item)) {
            selectedPlacesList.add(item)
        } else {
            selectedPlacesList.remove(item)
        }

        updateMarkers()
        selectRecyclerAdapter.notifyDataSetChanged() // RecyclerView 어댑터에 데이터 변경 알림
    }

    override fun onListBtnClick(position: Int, updatedSelectedPlacesList: ArrayList<SearchData>) {
        tMapView.removeAllTMapMarkerItem()
        tMapView.removeAllTMapPOIItem()
        selectedPlacesList.clear()
        selectedPlacesList.addAll(updatedSelectedPlacesList)
        updateMarkers()
        selectRecyclerAdapter.notifyDataSetChanged() // RecyclerView 어댑터에 데이터 변경 알림
        Log.d("ClickedItems", selectedPlacesList.toString())
    }

    fun onItemDelete(position: Int) {
        selectedPlacesList.removeAt(position)
     //   selectRecyclerAdapter.removeItem(selectPlaceList.get(position))
        updateMarkers()
    }

}