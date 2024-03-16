package com.example.kakaotest.Map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.kakaotest.Plan.SelectedPlaceData
import com.example.kakaotest.R

class RouteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        val logListView: ListView = findViewById(R.id.logListView)

        // 이전 액티비티로부터 전달받은 로그 리스트 가져오기
        val logList = intent.getStringArrayListExtra("logList")


        // 가져온 로그 리스트를 어댑터에 연결하여 리스트뷰에 출력
        val adapter = ArrayAdapter(this, R.layout.item_log, R.id.logTextView, logList ?: emptyList())


        logListView.adapter = adapter
    }
    }
