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
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.poi.TMapPOIItem
import kotlinx.coroutines.MainScope


class RestaurantDetailActivity : AppCompatActivity() {
    private val tourApiManager = TourApiManager()
    private val scope = MainScope()
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
        //container.addView(tMapView)

        // 발급받은 키로 TMapView에 API 키 설정
        //tMapView.setSKTMapApiKey(appKey)

        val tMapData = TMapData()
        val tMapPOIItem = TMapPOIItem()
        val marker = TMapMarkerItem()

        // Restaurant 객체를 인텐트에서 가져옴
        val restaurant = intent.getSerializableExtra("restaurant") as? Restaurant



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


        binding.button.setOnClickListener {
            //클릭하면 해당 장소를 기기에 저장
            // SharedPreferences 객체 가져오기
            val sharedPreferences = getSharedPreferences("MySavedRestaurants", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()




            // 사용자가 선택한 음식점의 정보를 SharedPreferences에 저장
            if (restaurant != null) {
                val mapx = String.format("%.8f", restaurant.mapx.toDouble())
                val mapy = String.format("%.8f", restaurant.mapy.toDouble())
                //Log.d("add_test","DetailActivity: ${mapx}_${mapy}")
                editor.putString("restaurant_${mapx}_${mapy}_${restaurant.addr1}", restaurant.title)
            }
            editor.apply()
        }
    }
    private suspend fun searchCategory(cat1: String, cat2: String, cat3: String): Array<String> {

        val restaurants = tourApiManager.searchCategory(cat1,cat2,cat3)
        // 결과 처리
        val items = restaurants.response.body.items
        val cate = arrayOf(items.item[0].cat1,items.item[0].cat1,items.item[0].cat1)
        return cate
    }
}