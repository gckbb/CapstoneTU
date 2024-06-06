package com.example.kakaotest.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kakaotest.HomeActivity
import com.example.kakaotest.R
import com.example.kakaotest.databinding.ActivityReadMyPostBinding
import com.example.kakaotest.databinding.ActivityReadPostBinding
import com.example.kakaotest.databinding.ActivityWritePostBinding

class ReadMyPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadMyPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadMyPostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //수정
        binding.edite.setOnClickListener {

        }

        //삭제
        binding.delete.setOnClickListener {

        }


        //뒤로가기
        binding.backBtn.setOnClickListener {
            finish()
        }
        //홈
        binding.home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }


    }
}