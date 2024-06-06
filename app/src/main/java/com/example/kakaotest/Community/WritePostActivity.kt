package com.example.kakaotest.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kakaotest.HomeActivity
import com.example.kakaotest.R
import com.example.kakaotest.databinding.ActivityCashBookBinding
import com.example.kakaotest.databinding.ActivityMainCommunityBinding
import com.example.kakaotest.databinding.ActivityWritePostBinding
import java.text.SimpleDateFormat

class WritePostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWritePostBinding
    private var uid:String? = null
    val dbTool = PostDB()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritePostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //uid 받음
        if(intent.hasExtra("uid")){
            uid = intent.getStringExtra("uid")
        }

        //작성 완료 버튼
        binding.nextbutton.setOnClickListener {
            val postTitle = binding.title.text.toString()
            val postContent = binding.content.text.toString()
            val postPhoto = null
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis())

            dbTool.AddPost(postTitle, postContent, postPhoto, timestamp, uid!!)
            Toast.makeText(this, "upload success", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainCommunity::class.java)
            startActivity(intent)
        }

        //뒤로가기
        binding.backBtn.setOnClickListener{
            finish()
        }

        //홈
        binding.home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }
}