package com.example.kakaotest.CheckList

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.CheckList.OptionSelectFragment.OnListSelectListener
import com.example.kakaotest.Utility.Adapter.CAdapter
import com.example.kakaotest.DataModel.CheckList.CheckListDB
import com.example.kakaotest.DataModel.CheckList.CheckListData
import com.example.kakaotest.R
import com.example.kakaotest.databinding.ActivityCheckListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface DataPassListener {
    fun onDataPassed(listTitle: String, currentDate: String, sNum: Int)
}


class CheckListActivity : AppCompatActivity(),DataPassListener {
    private lateinit var binding: ActivityCheckListBinding
    private lateinit var cadapter: CAdapter
    val itemList = ArrayList<CheckListData>()
    val dbTool = CheckListDB()
     val ADD_CHECKLIST_REQUEST = 1 // 요청 코드 상수로 정의
     private var isFragmentVisible = false

    private var optionSelectFragment: OptionSelectFragment? = null

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
        cadapter.setItemClickListener(object : CAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int, titleName: String) {
                Toast.makeText(this@CheckListActivity, "$titleName", Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.IO).launch {
                    val intent =
                        Intent(this@CheckListActivity, AfterSelectListActivity::class.java).apply {
                            putExtra("titleName", titleName)
                        }
                }
                startActivity(intent)
            }

            override fun onItemClick(view: View, position: Int, item: CheckListData) {
                val intent =
                    Intent(this@CheckListActivity, AfterSelectListActivity::class.java).apply {
                        putExtra("titleName", item.listName)
                    }
                startActivity(intent)
            }
        })


        //리스트 생성누르면 여행 분류 선택
        binding.fabAddCheckList.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val overlay = findViewById<View>(R.id.overlay)

            if (optionSelectFragment != null && isFragmentVisible) {
                fragmentManager.beginTransaction().remove(optionSelectFragment!!).commit()

                overlay.visibility = View.GONE
                isFragmentVisible = false
            } else if (optionSelectFragment== null || !isFragmentVisible) {
                optionSelectFragment = OptionSelectFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, optionSelectFragment!!)
                    .addToBackStack(null)
                    .commit()

                overlay.visibility = View.VISIBLE
                isFragmentVisible = true
            }
        }


        //여행분류 다녀온 후
        val requestActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val data = it.data
                    val listTitle = data?.getStringExtra("listTitle")
                    val currentDate = data?.getStringExtra("currentDate")
                    val sNum = data?.getIntExtra("sNum", 0)
                    onDataPassed(listTitle!!, currentDate!!, sNum!!)


                }
            }
    }
    fun hideOverlay() {
        val overlay = findViewById<View>(R.id.overlay)
        overlay.visibility = View.GONE
    }


    override fun onDataPassed(listTitle: String, currentDate: String, sNum: Int) {
        Toast.makeText(this, "List Selected: $listTitle, Date: $currentDate, Num: $sNum", Toast.LENGTH_SHORT).show()


            supportFragmentManager.beginTransaction().remove(optionSelectFragment!!).commit()
            isFragmentVisible = false
            optionSelectFragment = null


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