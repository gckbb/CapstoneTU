package com.example.kakaotest.CheckList

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.R
import com.example.kakaotest.databinding.ActivityCheckListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckListBinding
    private lateinit var cadapter: CAdapter
    val itemList = ArrayList<CheckListData>()
    val dbTool = CheckListDB()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val rv_checklist = findViewById<RecyclerView>(R.id.rvCheckList)
        //갱신
        cadapter = CAdapter(itemList)
        cadapter.notifyDataSetChanged()
        //어댑터
        rv_checklist.adapter = cadapter
        rv_checklist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //이미 생성된 리스트 선택하면 해당 체크리스트 화면으로 가야함
        cadapter.setItemClickListener(object: CAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, titleName: String) {
                Toast.makeText(this@CheckListActivity, "$titleName", Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.IO).launch {
                    val intent = Intent(this@CheckListActivity, AfterSelectListActivity::class.java).apply{
                        putExtra("titleName", titleName)
                    }
                }
                startActivity(intent)
            }

            override fun onItemClick(view: View, position: Int, item: CheckListData) {
                val intent = Intent(this@CheckListActivity, AfterSelectListActivity::class.java).apply{
                    putExtra("titleName", item.listName)
                }
                startActivity(intent)
            }
        })


        //리스트 생성누르면 여행 분류 선택
        binding.fabAddCheckList.setOnClickListener {
            val intent = Intent(this, ListSelectActivity::class.java).apply {
                putExtra("type", "ADD")
            }
            requestActivity.launch(intent)
        }

    }

    //여행분류 다녀온 후
    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            val data = it.data
            val listTitle = data?.getStringExtra("listTitle")
            val currentDate = data?.getStringExtra("currentDate")
            val sNum = data?.getIntExtra("sNum", 0)
            val dbTool = CheckListDB()

            //리스트 이름, 생성일자 넣어서 목록 일단 생성
            dbTool.initCheckList(listTitle!!, currentDate!!);
            when (sNum) {
                1 -> dbTool.sNum1(listTitle)
                2 -> dbTool.sNum2(listTitle)
                3 -> dbTool.sNum3(listTitle)
            }
            Toast.makeText(this, "목록 생성 완료", Toast.LENGTH_SHORT).show()

        }
    }


}