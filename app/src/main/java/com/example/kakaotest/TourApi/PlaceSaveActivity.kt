package com.example.kakaotest.TourApi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kakaotest.HomeActivity
import com.example.kakaotest.R
import com.example.kakaotest.databinding.ActivityPlaceSaveBinding
import com.example.kakaotest.databinding.ActivityTourapiBinding

class PlaceSaveActivity : AppCompatActivity() { //장소추천에서 저장한거 보는 화면
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityPlaceSaveBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }


    }
}