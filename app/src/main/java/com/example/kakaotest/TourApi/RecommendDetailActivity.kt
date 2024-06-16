package com.example.kakaotest.TourApi

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kakaotest.DataModel.Recommend
import com.example.kakaotest.DataModel.RecommendResponse
import com.example.kakaotest.R
import com.example.kakaotest.databinding.ActivityRecommendDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecommendDetailActivity : AppCompatActivity() {
    private val tourApiManager = TourApiManager()
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRecommendDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Restaurant 객체를 인텐트에서 가져옴
        val recommend = intent.getSerializableExtra("recommend") as? Recommend

        if(recommend != null) {
            getDetailIntro_39(recommend.contentid,recommend.contenttypeid)
            Glide.with(this)
                .load(recommend.firstimage)
                .into(binding.mainImage)

            binding.textViewTitle.text = recommend.title
            binding.textViewAddress.text = recommend.addr1
            binding.textViewTel.text = recommend.tel
            binding.textViewAddressDetail.text = recommend.addr2
            Log.d("Recommend","searchCategory 실행")
            searchCategory(recommend.cat1,recommend.cat2,recommend.cat3)
        }


        binding.button.setOnClickListener {
            //클릭하면 해당 장소를 기기에 저장
            // SharedPreferences 객체 가져오기
            val sharedPreferences = getSharedPreferences("MySavedRestaurants", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            // 사용자가 선택한 음식점의 정보를 SharedPreferences에 저장
            if (recommend != null) {
                val mapx = String.format("%.8f", recommend.mapx.toDouble())
                val mapy = String.format("%.8f", recommend.mapy.toDouble())
                //Log.d("add_test","DetailActivity: ${mapx}_${mapy}")
                editor.putString("recommend_${mapx}_${mapy}_${recommend.addr1}", recommend.title)
            }
            editor.apply()
        }
    }
    private fun searchCategory(cat1: String, cat2: String, cat3: String)  {
        scope.launch {
            try {
                Log.d("Restaurant","tourApiManager.searchCategory(cat1,cat2,cat3)")
                val cate_1 = tourApiManager.searchCategory(cat1,cat2,cat3)
                val cate_3 = tourApiManager.searchCategory3(cat1)
                val cate_4 = tourApiManager.searchCategory4()
                var cate_items_1 = cate_1.response.body.items.item
                var cate_items_3 = cate_3.response.body.items.item
                var cate_items_4 = cate_4.response.body.items.item

                var cate1 : TextView = findViewById(R.id.cat1)
                for(item in cate_items_4){
                    if(item.code.equals(cat1))
                        cate1.text = item.name
                }
                Log.d("Restaurant","${cate1.text}")
                var cate2 : TextView = findViewById(R.id.cat2)
                for(item in cate_items_3){
                    if(item.code == cat2)
                        cate2.text = item.name
                }
                Log.d("Restaurant","${cate2.text}")
                var cate3 : TextView = findViewById(R.id.cat3)
                cate3.text = cate_items_1[0].name
                Log.d("Restaurant","${cate3.text}")

            } catch (e: Exception) {
                // 오류 처리
                Log.e("TourApiActivity", "음식점 검색 오류: $e")
            }
        }
    }

    private fun getDetailIntro_39(contentId: String, contentTypeId: String) {
        scope.launch {
            try {
                // API를 통해 데이터 가져오기
                Log.d("detailCode","contentId: ${contentId} 실행")
                val details: RecommendResponse = tourApiManager.getDetailIntro(contentId, contentTypeId)
                Log.d("detailCode","getDetailIntro 실행")
                var details_info = details.response.body.items.item[0]
                Log.d("detailCode","${details_info}")
                // UI 업데이트를 위해 메인 스레드에서 처리
                withContext(Dispatchers.Main) {
                    updateUIWithDetails(details_info)
                }

            } catch (e: Exception) {
                Log.e("Exception", "기타 오류: $e")
            }
        }
    }

    private fun updateUIWithDetails(details_info: Recommend) {
        val binding = ActivityRecommendDetailBinding.inflate(layoutInflater)
        // 각 TextView에 데이터 설정
        binding.chkcreditcardfood.text = details_info.chkcreditcardfood ?: "정보 없음"
        binding.discountinfofood.text = details_info.discountinfofood ?: "정보 없음"
        binding.firstmenu.text = details_info.firstmenu ?: "정보 없음"
        binding.infocenterfood.text = details_info.infocenterfood ?: "정보 없음"
        binding.kidsfacility.text = details_info.kidsfacility ?: "정보 없음"
        binding.opendatefood.text = details_info.opendatefood ?: "정보 없음"
        binding.opentimefood.text = details_info.opentimefood ?: "정보 없음"
        binding.packing.text = details_info.packing ?: "정보 없음"
        binding.parkingfood.text = details_info.parkingfood ?: "정보 없음"
        binding.reservationfood.text = details_info.reservationfood ?: "정보 없음"
        binding.restdatefood.text = details_info.restdatefood ?: "정보 없음"
        binding.scalefood.text = details_info.scalefood ?: "정보 없음"
        binding.seat.text = details_info.seat ?: "정보 없음"
        binding.smoking.text = details_info.smoking ?: "정보 없음"
        binding.treatmenu.text = details_info.treatmenu ?: "정보 없음"
        binding.lcnsno.text = details_info.lcnsno ?: "정보 없음"
    }

}