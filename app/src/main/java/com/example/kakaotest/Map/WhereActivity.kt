package com.example.kakaotest.Map

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.kakaotest.DataModel.Place
import com.example.kakaotest.R
import com.example.kakaotest.Utility.Adapter.SearchRecyclerAdapter
import com.example.kakaotest.Utility.dialog.AlertDialogHelper
import com.example.kakaotest.Utility.tmap.RetrofitUtil
import com.example.kakaotest.databinding.ActivityWhereBinding
import com.example.kakaotest.response.search.Poi
import com.example.kakaotest.response.search.Pois
import com.example.kakaotest.search.LocationLatLngEntity
import com.example.kakaotest.search.SearchResultEntity
import com.skt.tmap.TMapPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.coroutines.CoroutineContext


class WhereActivity :  AppCompatActivity() , CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var binding:ActivityWhereBinding
    private lateinit var adapter: SearchRecyclerAdapter

    private var selectedPlace: Place? = null // 선택한 장소를 저장할 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_where)
        binding = ActivityWhereBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("PLAN","WhereActivity 화면 이동 성공")
        job = Job()

        val backBtn = findViewById<ImageButton>(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()

        }

        binding.nextbutton.setOnClickListener {
            // 다음 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this, PlanInfoInput::class.java)
            // 선택한 장소가 있을 때에만 인텐트에 추가
            selectedPlace?.let { place ->
                intent.putExtra("region", place)
                startActivity(intent)
            }
        }


        initAdapter()
        initViews()
        bindViews()
        initData()


    }
    private fun initAdapter() {
        adapter = SearchRecyclerAdapter()

    }
    private fun initViews() = with(binding){
        emptyResultTextView.isVisible = false
        placerecyclerView.adapter = adapter
    }
    private fun bindViews() = with(binding){
        searchBtn.setOnClickListener {
            val place_search=findViewById<EditText>(R.id.place_search)
            searchKeyword(place_search.text.toString())
        }
    }


    private fun initData(){
        adapter.notifyDataSetChanged()
    }

    private fun setData(pois: Pois) {
        val dataList = pois.poi.map {
            SearchResultEntity(
                name = it.name ?: "빌딩명 없음",
                address = makeMainAddress(it),
                locationLatLng = LocationLatLngEntity(
                    TMapPoint(it.noorLat, it.noorLon)
                )
            )
        }
        adapter.setSearchResultList(dataList) {
            Toast.makeText(
                this,
                "장소명 : ${it.name}, 주소 : ${it.address}, 위/경도 : ${it.locationLatLng}",
                Toast.LENGTH_SHORT
            ).show()
            val selectedPlace = Place(it.name, it.locationLatLng.tpoint, it.address)
            this@WhereActivity.selectedPlace = selectedPlace
            Log.d("PLAN", "지역 선택 : $selectedPlace")
            AlertDialogHelper().showAlertMessage(this,"${it.name}(으)로 여행 가시나요? . ","네","아니요",null,
                DialogInterface.OnClickListener { dialog, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        val intent = Intent(this, PlanInfoInput::class.java)
                        // 선택한 장소가 있을 때에만 인텐트에 추가
                        selectedPlace?.let { place ->
                            intent.putExtra("region", place)
                            startActivity(intent)
                        }

                    }
                else if (which==DialogInterface.BUTTON_NEGATIVE){
                        dialog.dismiss()
                }})

        }
    }



    private fun makeMainAddress(poi: Poi): String =
        if (poi.secondNo?.trim().isNullOrEmpty()){
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    poi.firstNo?.trim()
        } else {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    (poi.firstNo?.trim() ?: "") + " " +
                    poi.secondNo?.trim()
        }

    private fun searchKeyword(keywordString: String){
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO){
                    val response = RetrofitUtil.apiService.getSearchLocation(
                        keyword = keywordString
                    )
                    if(response.isSuccessful){
                        val body = response.body()
                        withContext(Dispatchers.Main){
                            Log.e("response", body.toString())
                            body?.let { searchResponse ->
                                setData(searchResponse.searchPoiInfo.pois)
                            }
                        }
                    }
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    //private fun ListViewVisibility() {
    //  if (placeListView.visibility != View.VISIBLE) {
    //    placeListView.visibility = View.VISIBLE
    //}
    //}
}