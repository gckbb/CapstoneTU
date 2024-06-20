package com.example.kakaotest.CheckList

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.DataModel.CheckList.CheckListDB
import com.example.kakaotest.DataModel.CheckList.CheckListData
import com.example.kakaotest.databinding.ActivityAfterSelectListBinding
import com.example.kakaotest.Utility.Adapter.TodoAdapter
import com.example.kakaotest.DataModel.CheckList.TodoListData
import com.example.kakaotest.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AfterSelectListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAfterSelectListBinding
    private lateinit var todoadapter: TodoAdapter
    private lateinit var todoTitle: String
    private val itemList = ArrayList<TodoListData>()
    private val dbTool = CheckListDB()

    // Firebase 초기화
    private val database = FirebaseDatabase.getInstance()
    private val dbReference = database.getReference("checklists")
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterSelectListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val listTitle = intent.getStringExtra("titleName")
        todoTitle = listTitle!!

        binding.tvListTitle.text = listTitle
        val rv_todolist = findViewById<RecyclerView>(R.id.rvTodoList)

        // RecyclerView 설정
        todoadapter = TodoAdapter(itemList, todoTitle)
        rv_todolist.adapter = todoadapter
        rv_todolist.layoutManager = LinearLayoutManager(this)

        // 추가 버튼 클릭 시 EditTodoActivity로 이동
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, EditTodoActivity::class.java).apply {
                putExtra("type", "ADD")
                putExtra("listTitle", listTitle)
            }
            requestActivity.launch(intent)
        }

        // 뒤로가기 버튼
        binding.backBtn.setOnClickListener {
            finish()
        }

        // 이미 생성된 리스트 선택 시 해당 투두리스트 화면으로 이동
        todoadapter.setItemClickListener(object : TodoAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int, titleName: String) {
            }

            override fun onItemClick(view: View, position: Int, item: TodoListData) {
                // 투두리스트 수정
                val intent = Intent(this@AfterSelectListActivity, EditTodoActivity::class.java).apply {
                    putExtra("title", item.todoTitle)
                    putExtra("content", item.todoContent)
                    putExtra("type", "EDIT")
                }
                startActivity(intent)
            }
        })

        // 체크박스 토글 기능
        todoadapter.setItemCheckBoxClickListener(object : TodoAdapter.ItemCheckBoxClickListener {
            override fun onClick(view: View, position: Int, item: TodoListData) {
                CoroutineScope(Dispatchers.IO).launch {
                    val currentItem = itemList[position]
                    currentItem.isChecked = !currentItem.isChecked!!
                    dbTool.UpdateChecked(todoTitle!!, currentItem.isChecked!!, currentItem.todoTitle!!)
                }
            }
        })

        // 공유하기 버튼 클릭 리스너 설정
        binding.btnShare.setOnClickListener {
            // 공유 기능 실행
            Log.d("sharecode","tvListTitle: ${binding.tvListTitle.text}")
            accessCheckListViaListTitle(binding.tvListTitle.text.toString())
        }
    }

    // EditTodoActivity에서 돌아온 후 처리
    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val title = data?.getStringExtra("title")
            val content = data?.getStringExtra("content")

            when (result.data?.getIntExtra("flag", -1)) {
                0 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        dbTool.AddTodo(todoTitle!!, title!!, content!!, false)
                    }
                    Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 공유 기능 실행 함수
    private fun shareList(sharingCode: String) {
        // 공유 코드를 사용하여 공유 메시지 생성
        val shareMessage = sharingCode

        // 공유 인텐트 생성
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "체크리스트 공유하기")
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }

        // 공유 인텐트 실행
        startActivity(Intent.createChooser(shareIntent, "공유하기"))
    }

    // 리스트 제목을 통해 Firestore에서 체크리스트 가져오기
    fun accessCheckListViaListTitle(listTitle: String) {
        firestore.collection("checklists")
            .whereEqualTo("listName", listTitle)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "이름 오류.", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in documents) {
                        val checkList = document.toObject(CheckListData::class.java)
                        val sharecode = checkList.sharecode
                        showShareCodeDialog(sharecode!!)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "체크리스트 접근 실패", Toast.LENGTH_SHORT).show()
            }
    }

    // 공유 코드를 표시하는 다이얼로그
    fun showShareCodeDialog(sharecode: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("공유 코드")

        builder.setMessage("공유 코드: $sharecode")

        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }
}
