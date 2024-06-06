package com.example.kakaotest.CashBook


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.CheckList.DataPassListener
import com.example.kakaotest.CheckList.OptionSelectFragment
import com.example.kakaotest.DataModel.CashBook.CashListData
import com.example.kakaotest.DataModel.CashBook.CashbookDB
import com.example.kakaotest.DataModel.CheckList.CheckListDB
import com.example.kakaotest.R
import com.example.kakaotest.Utility.Adapter.CashListAdapter
import com.example.kakaotest.databinding.ActivityCashBookBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


interface DataPassListener {
    fun onDataPassed(listTitle: String, currentDate: String, sNum: Int)
}

class CashBookActivity : AppCompatActivity() , DataPassListener {
    private lateinit var binding: ActivityCashBookBinding
    private lateinit var cadapter: CashListAdapter
    val itemList = ArrayList<CashListData>()
    val dbTool = CashbookDB()

    val ADD_CHECKLIST_REQUEST = 1 // 요청 코드 상수로 정의
    private var isFragmentVisible = false

    private var optionSelectFragment : CashOptionSelectFragment?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCashBookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val rv_cashlist = findViewById<RecyclerView>(R.id.rvCashList)
        //갱신
        cadapter = CashListAdapter(itemList)
        cadapter.notifyDataSetChanged()
        //어댑터
        rv_cashlist.adapter = cadapter
        rv_cashlist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //이미 생성된 리스트 선택하면 해당  화면으로 가야함
        cadapter.setItemClickListener(object: CashListAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, titleName: String) {
                Toast.makeText(this@CashBookActivity, "$titleName", Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.IO).launch {
                    val intent = Intent(this@CashBookActivity, AfterCashActivity::class.java).apply{
                        putExtra("titleName", titleName)
                    }
                }
                startActivity(intent)
            }

            override fun onItemClick(view: View, position: Int, item: CashListData) {
                val intent = Intent(this@CashBookActivity, AfterCashActivity::class.java).apply{
                    putExtra("titleName", item.listName)
                }
                startActivity(intent)
            }
        })



        binding.backBtn.setOnClickListener {
            finish()

        }
        binding.fabAddCashList.setOnClickListener{
            val fragmentManager = supportFragmentManager
            if (optionSelectFragment != null && isFragmentVisible) {
                fragmentManager.beginTransaction().remove(optionSelectFragment!!).commit()
                hideOverlay()
                isFragmentVisible = false

            } else if (optionSelectFragment== null || !isFragmentVisible) {
                optionSelectFragment = CashOptionSelectFragment()

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, optionSelectFragment!!)
                    .addToBackStack(null)
                    .commit()

                val overlay = findViewById<View>(R.id.overlay)
                overlay.visibility = View.VISIBLE
                isFragmentVisible = true
            }
        }



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
    override fun onBackPressed() {
        val overlay = findViewById<View>(R.id.overlay)
        if (supportFragmentManager.backStackEntryCount > 0) {
            overlay.visibility = View.GONE
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun showFragment(fragment: Fragment) {
        val overlay = findViewById<View>(R.id.overlay)
        overlay.visibility = View.VISIBLE

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

   override  fun onDataPassed(listTitle: String, currentDate: String, sNum: Int) {
        Toast.makeText(this, "List Selected: $listTitle, Date: $currentDate, Num: $sNum", Toast.LENGTH_SHORT).show()


        supportFragmentManager.beginTransaction().remove(optionSelectFragment!!).commit()
        isFragmentVisible = false
        optionSelectFragment = null


        val dbTool = CashbookDB()


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