package com.example.kakaotest.Community

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.kakaotest.HomeActivity
import com.example.kakaotest.R
import com.example.kakaotest.databinding.ActivityMainCommunityBinding
import com.example.kakaotest.databinding.ActivityReadPostBinding

class ReadPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadPostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val postTitle = intent.getStringExtra("postTitle")
        val postContent = intent.getStringExtra("postContent")
        val postPhoto = intent.getStringExtra("postPhoto")
        val timestamp = intent.getStringExtra("timestamp")
        val UID = intent.getStringExtra("UID")

        binding.title.text = postTitle
        binding.content.text = postContent
        binding.timestamp.text = timestamp
        binding.userId.text = UID
        Log.d("photo", "photo: $postPhoto")
        // 이미지 URI를 Glide를 사용하여 이미지뷰에 표시
        if (postPhoto != null) {
            Glide.with(this)
                .load(postPhoto)
                .into(binding.image)
        }

        // 뒤로가기
        binding.backBtn.setOnClickListener{
            finish()
        }

        // 홈
        binding.home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }
}