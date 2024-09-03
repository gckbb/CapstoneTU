package com.example.kakaotest.Community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kakaotest.HomeActivity
import com.example.kakaotest.R
import com.example.kakaotest.databinding.ActivityReadPostBinding

class ReadPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadPostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Intent로부터 전달된 데이터 받기
        val postTitle = intent.getStringExtra("postTitle")
        val postContent = intent.getStringExtra("postContent")
        val postPhoto = intent.getStringExtra("postPhoto")
        val timestamp = intent.getStringExtra("timestamp")
        val UID = intent.getStringExtra("UID")

        // 데이터 설정
        binding.title.text = postTitle
        binding.content.text = postContent
        binding.timestamp.text = timestamp
        binding.userId.text = UID

        // 이미지 로드
        Log.d("photo", "photo: $postPhoto")
        if (postPhoto != null && postPhoto.isNotEmpty()) {
            Glide.with(this)
                .load(postPhoto)
                .error(R.drawable.null_image) // 로드 실패 시 기본 이미지 설정
                .into(binding.image)
        } else {
            // 이미지가 없을 경우 기본 이미지 설정 (선택 사항)
            binding.image.setImageResource(R.drawable.null_image)
        }

        // 뒤로가기 버튼
        binding.backBtn.setOnClickListener{
            finish()
        }

        // 홈 버튼
        binding.home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
