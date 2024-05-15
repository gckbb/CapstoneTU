package com.example.kakaotest.TourApi

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kakaotest.DataModel.Restaurant
import com.example.kakaotest.R
import com.example.kakaotest.databinding.ActivityRestaurantDetailBinding
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem

class RestaurantDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("app_key", "8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP").apply()

        // 값을 가져옴
        val appKey: String? = sharedPreferences.getString("app_key", null)

        // FrameLayout 컨테이너를 XML에서 찾아옴
        val container: FrameLayout = findViewById(R.id.tmapViewContainer)

        // TMapView 인스턴스를 생성
        val tMapView = TMapView(this)

        // TMapView를 FrameLayout에 추가
        container.addView(tMapView)

        // 발급받은 키로 TMapView에 API 키 설정
        tMapView.setSKTMapApiKey(appKey)

        val marker = TMapMarkerItem()

        // Restaurant 객체를 인텐트에서 가져옴
        val restaurant = intent.getSerializableExtra("restaurant") as? Restaurant

        var selcted = TMapPoint(restaurant!!.mapx.toDouble(),restaurant.mapy.toDouble())

        if(restaurant != null) {

            Glide.with(this)
                .load(restaurant.firstimage)
                .into(binding.mainImage)

            binding.textViewTitle.text = restaurant.title
            binding.textViewAddress.text = restaurant.addr1
            binding.textViewTitle.text = restaurant.tel
            binding.textViewAddressDetail.text = restaurant.addr2
            binding.cat1.text = restaurant.cat1
            binding.cat2.text = restaurant.cat2
            binding.cat3.text = restaurant.cat3
        }

        tMapView.setOnMapReadyListener(object : TMapView.OnMapReadyListener {
            override fun onMapReady() {
                // 맵 로딩이 완료된 후에 수행할 동작을 구현해주세요
                // 예: 마커 추가, 경로 표시 등
                Toast.makeText(this@RestaurantDetailActivity, "MapLoading", Toast.LENGTH_SHORT).show()

                if (restaurant != null) {
                    Log.d("restaurant","${restaurant.title}, ${restaurant.mapx}, ${restaurant.mapy.toDouble()}")
                    Log.d("restaurant","1.${tMapView.centerPoint}")
                    marker.setTMapPoint(selcted.latitude,selcted.longitude)
                    marker.id = "marker1"
                    marker.icon = BitmapFactory.decodeResource(resources, R.drawable.poi)
                    marker.visible = true

                    tMapView.addTMapMarkerItem(marker)
                    tMapView.setCenterPoint(selcted.latitude,selcted.longitude)
                    tMapView.setLocationPoint(selcted.latitude,selcted.longitude)

                    Log.d("restaurant","2.${tMapView.centerPoint}")
                }
                //tMapView.zoomLevel = 15

                binding.button.setOnClickListener {
                    Log.d("restaurant","${restaurant.title}, ${restaurant.mapx}, ${restaurant.mapy.toDouble()}")
                    Log.d("restaurant","3.${tMapView.centerPoint}")
                    marker.setTMapPoint(selcted.latitude,selcted.longitude)
                    marker.id = "marker1"
                    marker.icon = BitmapFactory.decodeResource(resources, R.drawable.poi)
                    marker.visible = true

                    tMapView.addTMapMarkerItem(marker)
                    tMapView.setCenterPoint(selcted.latitude,selcted.longitude)
                    tMapView.setLocationPoint(selcted.latitude,selcted.longitude)
                    tMapView.zoomLevel = 15

                    Log.d("restaurant","4.${tMapView.centerPoint}")
                }
            }
        })


    }
}
