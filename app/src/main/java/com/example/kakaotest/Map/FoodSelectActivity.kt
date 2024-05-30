package com.example.kakaotest.Map

import DataAdapter
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.Toast
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.tmap.SearchData
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.example.kakaotest.R
import com.example.kakaotest.Utility.dialog.AlertDialogHelper

import com.example.kakaotest.databinding.ActivityFoodSelectBinding
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
    private val binding get() = mBinding!!
    var startpoint: TMapPoint? = null
    var endpoint: TMapPoint? = null
    lateinit var start: TMapPoint



    lateinit var end: TMapPoint
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFoodSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val receivedDataList = intent.getParcelableArrayListExtra<SelectedPlaceData>("selectedPlaceDataList")
        val travelPlan = intent.getParcelableExtra<TravelPlan>("travelPlan")
        Log.d("ITEM",receivedDataList.toString())
        Log.d("ITEM", arrayListOf(receivedDataList).toString())


        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("app_key", "8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP").apply()

        // 값을 가져옴
        val appKey: String? = sharedPreferences.getString("app_key", null)

        // FrameLayout 컨테이너를 XML에서 찾아옴
        val container: FrameLayout = findViewById(R.id.tmapViewContainer)
        // TMapView 인스턴스를 생성
        val tMapView = TMapView(this)
        val tMapData = TMapData()
        val tMapGps = TMapPoint()


        // TMapView를 FrameLayout에 추가
        container.addView(tMapView)
        // 발급받은 키로 TMapView에 API 키 설정
        tMapView.setSKTMapApiKey(appKey)



        adapter = DataAdapter(this,R.layout.data_list, searchDataList,this)
        binding.searchDataListView.adapter = adapter


        binding.searchDataListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectItem = parent.getItemAtPosition(position) as SearchData


            tMapView.setCenterPoint(selectItem.tpoint.latitude,selectItem.tpoint.longitude)
            tMapView.zoomLevel = 15


            // 중복된 장소인지 확인
            if (!isPlaceAlreadySelected(selectItem)) {
                //  Toast.makeText(this, selectItem.id, Toast.LENGTH_SHORT).show()

                // 선택한 장소를 리스트에 추가
                selectedFoodPlacesList.add(selectItem)

                Log.d("ITEM", selectedFoodPlacesList.toString())

                // 선택한 장소를 SelectedPlaceData에 추가
                val selectedPlaceData = SelectedPlaceData(
                    placeName = selectItem.id,
                    tpoint = TMapPoint(selectItem.tpoint.latitude, selectItem.tpoint.longitude),
                    address = selectItem.address

                )

                receivedDataList?.add(selectedPlaceData)



            } else {

                AlertDialogHelper().showAlertMessage(this,"선택 취소하시겠습니까? ","네","아니요",null,
                    DialogInterface.OnClickListener { dialog, which ->
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            selectedFoodPlacesList.remove(selectItem)
                            Log.d("PLAN","장소 선택 취소 : $selectedFoodPlacesList")
                        }else if (which == DialogInterface.BUTTON_NEGATIVE){
                            dialog.dismiss()
                        }
                    })
            }

            Log.d("PLAN",receivedDataList.toString())

        }


        // 클릭 이벤트 설정
        tMapView.setOnClickListenerCallback(object : TMapView.OnClickListenerCallback {
            override fun onPressDown( // 터치함
                p0: java.util.ArrayList<TMapMarkerItem>?,
                p1: java.util.ArrayList<TMapPOIItem>?,
                p2: TMapPoint?,
                p3: PointF?
            ) {
                Toast.makeText(this@FoodSelectActivity, "onPressDown", Toast.LENGTH_SHORT).show()
            }

            override fun onPressUp( // 떨어짐
                p0: java.util.ArrayList<TMapMarkerItem>?,
                p1: java.util.ArrayList<TMapPOIItem>?,
                p2: TMapPoint?,
                p3: PointF?
            ) {
                Toast.makeText(this@FoodSelectActivity, "onPressUp", Toast.LENGTH_SHORT).show()
            }
        })




        // Map 로딩 완료 리스너 설정
        tMapView.setOnMapReadyListener {
            tMapView.setCenterPoint(126.9780, 37.5665)
            tMapView.zoomLevel = 10
        }

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
                })
            }   catch (e: Exception) {
                Log.e("FoodSelectActivity", "searchBtn action - Exception: ${e.toString()}", e)
            }
        }

        binding.nextbutton.setOnClickListener {
            val intent = Intent(this, RouteListActivity::class.java)
            intent.putExtra("travelPlan", travelPlan)
            intent.putParcelableArrayListExtra("selectedPlaceDataList", receivedDataList)
            Log.d("receivedDataList", " receivedDataList : " + receivedDataList.toString())
            intent.putParcelableArrayListExtra("selectedFoodDataList", ArrayList(selectedFoodPlacesList))


            startActivity(intent)
            Log.d("selectedFoodPlacesList ","selectedFoodPlacesList :  "+selectedFoodPlacesList.toString())

        }




    }
    override fun onItemClick(item: SearchData) {
        if (! selectedFoodPlacesList.contains(item)) {
            selectedFoodPlacesList.add(item)
            Toast.makeText(this, "${item.id} 추가", Toast.LENGTH_SHORT).show()

        } else {
            selectedFoodPlacesList.remove(item)
            Toast.makeText(this, "${item.id} 삭제", Toast.LENGTH_SHORT).show()

        }
    }
    override fun onListBtnClick(position: Int, updatedSelectedPlacesList: ArrayList<SearchData>) {
        selectedFoodPlacesList.clear()
        selectedFoodPlacesList.addAll(updatedSelectedPlacesList)
        Toast.makeText(this, "아이템 ${position + 1} 클릭됨", Toast.LENGTH_SHORT).show()
        Log.d("ClickedItems",  selectedFoodPlacesList.toString())
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