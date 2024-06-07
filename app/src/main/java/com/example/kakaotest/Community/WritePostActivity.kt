package com.example.kakaotest.Community

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

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
            //val postPhoto = null
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis())

            if(postTitle.isNotEmpty() && postContent.isNotEmpty()) {
                dbTool.AddPost(postTitle, postContent, imageUri.toString(), timestamp, uid!!)
                //dbTool.AddPost(postTitle, postContent, postPhoto, timestamp, uid!!)
                Toast.makeText(this, "upload success", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainCommunity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "제목, 내용 입력하세요!", Toast.LENGTH_SHORT).show()
            }
        }

        //이미지 선택
        binding.image.setOnClickListener {
            openGallery()
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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            binding.image.setImageURI(imageUri)
        }
    }
}