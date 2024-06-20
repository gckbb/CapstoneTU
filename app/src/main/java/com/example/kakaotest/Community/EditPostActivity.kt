package com.example.kakaotest.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kakaotest.HomeActivity
import com.example.kakaotest.Map.SelectedPlace
import com.example.kakaotest.R
import com.example.kakaotest.databinding.ActivityEditPostBinding
import com.example.kakaotest.databinding.ActivityMapBinding

class EditPostActivity : AppCompatActivity() {

    private var mBinding:  ActivityEditPostBinding?=null
    private val binding get() = mBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backBtn.setOnClickListener {
            val intent = Intent(this, ReadMyPostActivity::class.java)
            startActivity(intent)
        }

        binding.home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        //binding.image //사진 업로드

        /*binding.imageupload.setOnClickListener{   //사진 업로드 버튼?
          //사진 업로드할 때



          // 사진 업로드되면 사라지게 , 등록되어있을때도
          imageupload.visibility = View.GONE
        }
         */


        //binding.title  //제목 수정

        //binding.content  //내용 수정


        /*binding.nextbutton.setOnClickListener {
           //수정된 내용


            val intent = Intent(this, ReadMyPostActivity::class.java) //수정내용 확인
            startActivity(intent)

         */

    }
}