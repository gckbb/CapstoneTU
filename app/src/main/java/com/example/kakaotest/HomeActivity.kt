package com.example.kakaotest


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.Map.WhereActivity
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


        val firstList = intent.getParcelableArrayListExtra<SearchRouteData>("firstList")
        Log.d("PLAN", "firstRoute \n" + firstList.toString())
    }



}