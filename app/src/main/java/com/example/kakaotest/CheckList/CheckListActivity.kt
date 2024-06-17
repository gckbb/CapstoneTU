package com.example.kakaotest.CheckList

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface DataPassListener {
    fun onDataPassed(listTitle: String, currentDate: String, sNum: Int)
}

class CheckListActivity : AppCompatActivity(), DataPassListener {
    private lateinit var binding: ActivityCheckListBinding
    private lateinit var cadapter: CAdapter
    val itemList = ArrayList<CheckListData>()
    val dbTool = CheckListDB()
    private var isFragmentVisible = false

    private var optionSelectFragment: OptionSelectFragment? = null

    // Firebase 초기화
    private val database = FirebaseDatabase.getInstance()
    private val dbReference = database.getReference("checklists")
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val rv_checklist = findViewById<RecyclerView>(R.id.rvCheckList)
        // 갱신
        cadapter = CAdapter(itemList)
        cadapter.notifyDataSetChanged()
        // 어댑터
        rv_checklist.adapter = cadapter
        rv_checklist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // 이미 생성된 리스트 선택하면 해당 체크리스트 화면으로 가야함
        cadapter.setItemClickListener(object : CAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int, titleName: String) {
                Toast.makeText(this@CheckListActivity, "$titleName", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@CheckListActivity, AfterSelectListActivity::class.java).apply {
                    putExtra("titleName", titleName)
                }
                startActivity(intent)
            }

            override fun onItemClick(view: View, position: Int, item: CheckListData) {
                val intent = Intent(this@CheckListActivity, AfterSelectListActivity::class.java).apply {
                    putExtra("titleName", item.listName)
                }
                startActivity(intent)
            }
        })

        // 리스트 생성 누르면 여행 분류 선택
        binding.fabAddCheckList.setOnClickListener {
            val fragmentManager = supportFragmentManager
            if (optionSelectFragment != null && isFragmentVisible) {
                fragmentManager.beginTransaction().remove(optionSelectFragment!!).commit()
                isFragmentVisible = false
            } else if (optionSelectFragment == null || !isFragmentVisible) {
                optionSelectFragment = OptionSelectFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, optionSelectFragment!!)
                    .commit()
                isFragmentVisible = true
            }
        }

        binding.fabshareCheckList.setOnClickListener {
            showShareCodeDialog()
        }

        // 뒤로가기 버튼
        binding.backBtn.setOnClickListener {
            finish()
        }

        // 여행 분류 다녀온 후
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

    override fun onDataPassed(listTitle: String, currentDate: String, sNum: Int) {
        Toast.makeText(this, "List Selected: $listTitle, Date: $currentDate, Num: $sNum", Toast.LENGTH_SHORT).show()
        supportFragmentManager.beginTransaction().remove(optionSelectFragment!!).commit()
        isFragmentVisible = false
        optionSelectFragment = null

        // 공유 코드 생성
        val shareCode = generateSharingCode(6)

        // 체크리스트 생성
        val newList = CheckListData(listTitle, currentDate, shareCode)

        // Firebase Database에 체크리스트 저장
        dbReference.child(listTitle).setValue(newList)

        // Firestore에 체크리스트 저장
        firestore.collection("checklists").document(listTitle).set(newList)
            .addOnSuccessListener {
                Toast.makeText(this, "목록 생성 완료. 공유 코드: $shareCode", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "목록 생성 실패", Toast.LENGTH_SHORT).show()
            }

        // 리스트 이름, 생성 일자 넣어서 목록 일단 생성
        dbTool.initCheckList(listTitle, currentDate)
        when (sNum) {
            1 -> dbTool.sNum1(listTitle)
            2 -> dbTool.sNum2(listTitle)
            3 -> dbTool.sNum3(listTitle)
        }
        Toast.makeText(this, "목록 생성 완료", Toast.LENGTH_SHORT).show()
    }

    fun showShareCodeDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("공유 코드 입력")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("확인") { dialog, _ ->
            val shareCode = input.text.toString()
            accessCheckListViaShareCode(shareCode)
            dialog.dismiss()
        }
        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    fun accessCheckListViaShareCode(shareCode: String) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("checklists")
            .whereEqualTo("sharecode", shareCode)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "잘못된 코드입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in documents) {
                        val checkList = document.toObject(CheckListData::class.java)
                        Toast.makeText(this, "체크리스트: ${checkList.listName}에 접근합니다.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, AfterSelectListActivity::class.java).apply {
                            putExtra("titleName", checkList.listName)
                        }
                        startActivity(intent)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "체크리스트 접근 실패", Toast.LENGTH_SHORT).show()
            }
    }

    private fun generateSharingCode(length: Int): String {
        val allowedChars = ('A'..'Z') + ('0'..'9') // 허용된 문자 범위
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun loadUserCheckLists() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("checklists")
            .whereArrayContains("userIds", userId)
            .get()
            .addOnSuccessListener { documents ->
                itemList.clear()
                for (document in documents) {
                    val checkList = document.toObject(CheckListData::class.java)
                    itemList.add(checkList)
                }
                cadapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "체크리스트 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onResume() {
        super.onResume()
        loadUserCheckLists()
    }
}
