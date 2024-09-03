package com.example.kakaotest.TourApi

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.HomeActivity
import com.example.kakaotest.R

class TourApiMyplaces : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var savedRestaurantNames: MutableList<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mytourapi)

        val backBtn = findViewById<ImageButton>(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()
        }

        // 초기 데이터 가져오기 및 ListView 설정
        val foundListView: ListView = findViewById(R.id.myplaces)
        savedRestaurantNames = getSavedRestaurantNames()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, savedRestaurantNames)
        foundListView.adapter = adapter

        foundListView.setOnItemClickListener { parent, view, position, id ->
            showDeleteConfirmationDialog(position)
        }

        val homebtn = findViewById<ImageButton>(R.id.home)
        homebtn.setOnClickListener{
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getSavedRestaurantNames(): MutableList<String> {
        val sharedPreferences = getSharedPreferences("MySavedRestaurants", Context.MODE_PRIVATE)
        val savedRestaurantMap = sharedPreferences.all
        val names = mutableListOf<String>()

        for ((key, value) in savedRestaurantMap) {
            names.add(value.toString())
        }

        return names
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("삭제 확인")
            .setMessage("이 장소를 삭제하시겠습니까?")
            .setPositiveButton("확인") { dialog, which ->
                deleteRestaurant(position)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun deleteRestaurant(position: Int) {
        // SharedPreferences에서 삭제
        val sharedPreferences = getSharedPreferences("MySavedRestaurants", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // 삭제할 항목의 이름
        val restaurantName = savedRestaurantNames[position]

        // 전체 저장된 항목 중 삭제할 항목을 찾아서 삭제
        val allEntries = sharedPreferences.all
        for ((key, value) in allEntries) {
            if (value.toString() == restaurantName) {
                editor.remove(key)
                break
            }
        }

        editor.apply()

        // 리스트에서 해당 항목 삭제 및 업데이트
        savedRestaurantNames.removeAt(position)
        adapter.notifyDataSetChanged()
    }

}
