package com.example.kakaotest.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.CheckList.AfterSelectListActivity
import com.example.kakaotest.DataModel.CheckList.CheckListDB
import com.example.kakaotest.DataModel.CheckList.CheckListData
import com.example.kakaotest.HomeActivity
import com.example.kakaotest.R
import com.example.kakaotest.Utility.Adapter.CAdapter
import com.example.kakaotest.databinding.ActivityCheckListBinding
import com.example.kakaotest.databinding.ActivityMainCommunityBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainCommunity : AppCompatActivity() {
    private lateinit var binding: ActivityMainCommunityBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var postAdapter: PostAdapter
    val dbTool = CheckListDB()
    var itemList = ArrayList<PostData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainCommunityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //현재 로그인 계정정보 관련
        auth = FirebaseAuth.getInstance()
        var currentUID = auth.currentUser?.uid
        Toast.makeText(this, currentUID, Toast.LENGTH_SHORT).show()


        //리사이클러 뷰
        val rvpost = findViewById<RecyclerView>(R.id.post_rv)
        postAdapter = PostAdapter(itemList)
        postAdapter.notifyDataSetChanged()
        rvpost.adapter = postAdapter
        rvpost.layoutManager = LinearLayoutManager(this)


        //게시물 선택 시 해당 게시물 읽기 화면으로
        postAdapter.setItemClickListener(object : PostAdapter.ItemClickListener {
            override fun onItemClick(view: View, position: Int, item: PostData) {
                val intent =
                    Intent(this@MainCommunity, ReadPostActivity::class.java).apply {
                        putExtra("postTitle", item.postTitle)
                        putExtra("postContent", item.postContent)
                        putExtra("postPhoto", item.postPhoto)
                        putExtra("timestamp", item.timestamp)
                        putExtra("UID", item.uid)
                    }
                startActivity(intent)
            }
        })



        //추가 버튼
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, WritePostActivity::class.java)
            intent.putExtra("uid", currentUID)
            startActivity(intent)
        }

        //홈
        binding.home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }
}