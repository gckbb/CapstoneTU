package com.example.kakaotest.TourApi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kakaotest.DataModel.Restaurant
import com.example.kakaotest.databinding.ActivityRestaurantDetailBinding

class RestaurantDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Restaurant 객체를 인텐트에서 가져옴
        val restaurant = intent.getSerializableExtra("restaurant") as? Restaurant
        if(restaurant != null) {

            Glide.with(this)
                .load(restaurant.firstimage)
                .into(binding.mainImage)

            binding.textViewTitle.text = restaurant.title
            binding.textViewAddress.text = restaurant.addr1
            binding.textViewTitle.text = restaurant.tel
            binding.textViewAddressDetail.text = restaurant.addr2
            binding.cat1.text = restaurant.cat1
            binding.cat2.text = restaurant.cat2
            binding.cat3.text = restaurant.cat3

            binding.button.setOnClickListener {
                val intent = Intent(this,Detail_map_test::class.java).apply {
                    putExtra("restaurant",restaurant)
                }
                startActivity(intent)
            }
        }

    }
}
