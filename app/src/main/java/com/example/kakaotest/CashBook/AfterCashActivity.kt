package com.example.kakaotest.CashBook

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.DataModel.CashBook.CashbookDB
import com.example.kakaotest.DataModel.CashBook.CashbookData
import com.example.kakaotest.R
import com.example.kakaotest.Utility.Adapter.CashbookAdapter
import com.example.kakaotest.databinding.ActivityAfterCashBinding
import com.example.kakaotest.databinding.ActivityAfterSelectCashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface OnDataPassedListener {
    fun onDataPassed(data: Any)
}

class AfterCashActivity : AppCompatActivity(), CashbookAdapter.TotalCostListener,OnDataPassedListener {
    private lateinit var binding: ActivityAfterCashBinding
    private lateinit var todoadapter: CashbookAdapter
    private lateinit var todoTitle: String
    private val dbTool = CashbookDB()
    val itemList = ArrayList<CashbookData>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  ActivityAfterCashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val listTitle = intent.getStringExtra("titleName")
        todoTitle = listTitle!!

        binding.tvListTitle.text = listTitle
        Log.d("AfterCashActivity", "onCreate() called") // 로그 추가

        val rv_todolist = findViewById<RecyclerView>(R.id.rvTodoList)
        todoadapter = CashbookAdapter(ArrayList(), todoTitle, this)
        rv_todolist.adapter = todoadapter
        rv_todolist.layoutManager = LinearLayoutManager(this)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.btnKakaopay.setOnClickListener {
            val uri = Uri.parse("https://link.kakaopay.com/_/YXRLjcW")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        val edit_title = findViewById<EditText>(R.id.edit_title)
        val edit_cost = findViewById<EditText>(R.id.edit_cost)

        val rootLayout = findViewById<ConstraintLayout>(R.id.backgroundLayout)
        rootLayout.setOnClickListener {
            edit_title.clearFocus()
            edit_cost.clearFocus()
            hideKeyboard(rootLayout)
        }

        binding.addBtn.setOnClickListener {
            val title = edit_title.text.toString()
            val content = edit_cost.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                dbTool.AddTodo(todoTitle, title, content.toInt(), false)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AfterCashActivity, "추가되었습니다.", Toast.LENGTH_SHORT)
                        .show()
                    edit_title.text.clear()
                    edit_cost.text.clear()
                }
            }
        }

        todoadapter.setItemClickListener(object : CashbookAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int, titleName: String) {
                Toast.makeText(this@AfterCashActivity, "$titleName", Toast.LENGTH_SHORT).show()
                val fragment = EditCashFragment() // 여기에 프래그먼트 이름을 넣으세요
                val bundle = Bundle()
                bundle.putString("titleName", titleName)
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            override fun onItemClick(view: View, position: Int, item: CashbookData) {
                val fragment = EditCashFragment.newInstance(item)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }

        })

        //체크박스 토글기능
        todoadapter.setItemCheckBoxClickListener(object: CashbookAdapter.ItemCheckBoxClickListener{
            override fun onClick(view: View, position: Int, itemid: CashbookData) {
                CoroutineScope(Dispatchers.IO).launch {
                    val currentItem = itemList[position]
                    currentItem.isChecked = !currentItem.isChecked!!
                    dbTool.UpdateChecked(todoTitle!!, currentItem.isChecked!!, currentItem.itemName!!)
                }
            }
        })
    }

    override fun onTotalCostUpdated(totalCost: Int) {
        binding.totalWon.text = totalCost.toString()
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDataPassed(data: Any) {
        // 프래그먼트에서 전달된 데이터를 처리하는 작업을 수행합니다.
        if (data is CashbookData) {
            for (item in itemList) {
                if (item.itemName == data.itemName) {
                    // 아이템을 업데이트합니다.
                    item.itemCost = data.itemCost
                    // RecyclerView를 다시 그리도록 합니다.
                    todoadapter.notifyDataSetChanged()
                    break
                }
            }
        }
    }

}