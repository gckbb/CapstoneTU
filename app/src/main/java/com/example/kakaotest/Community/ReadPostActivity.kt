package com.example.kakaotest.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        //binding.image.text = postPhoto

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