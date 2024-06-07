package com.example.kakaotest


import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ScrollView

import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.Map.WhereActivity
import com.example.kakaotest.TourApi.TourApiActivity
import com.example.kakaotest.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 검색 버튼 클릭 리스너 설정
        binding.search.setOnClickListener {
            val intent = Intent(this, WhereActivity::class.java)
            startActivity(intent)
            Log.d("PLAN", "homefragment -> whereactivity")
        }


        binding.placeRecommendtxt.setOnClickListener {
            val intent = Intent(this, TourApiActivity::class.java)
            startActivity(intent)
        }






        binding.homefragmend.setOnScrollChangeListener{ v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.i(TAG, "scrolly : $scrollY");

            //스크롤 아래로
            if (scrollY > oldScrollY) {
                val anim = TranslateAnimation(0f, binding.floatBtn.width.toFloat(), 0f, 0f)
                anim.duration = 100
                binding.floatBtn.animation = anim
                binding.floatBtn.visibility = View.VISIBLE

                binding.floatBtn.setOnClickListener {
                    binding.homefragmend.fullScroll(ScrollView.FOCUS_UP)
                }
            }else {
                binding.floatBtn.visibility = View.GONE
            }
            }
    }



}