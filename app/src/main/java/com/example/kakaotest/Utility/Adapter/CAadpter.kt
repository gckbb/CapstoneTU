package com.example.kakaotest.Utility.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.DataModel.CheckList.CheckListDB
import com.example.kakaotest.DataModel.CheckList.CheckListData
import com.example.kakaotest.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class CAdapter(private var itemList: ArrayList<CheckListData>) : RecyclerView.Adapter<CAdapter.CheckListViewHolder>() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = database.reference.child("checklists")
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val dbTool = CheckListDB()

    init {
        // 초기 데이터 로드를 위한 함수 호출
        loadInitialData()
    }

    inner class CheckListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var listName: TextView = itemView.findViewById(R.id.tvTodoItem)
        var timestamp: TextView = itemView.findViewById(R.id.tvTimeStamp)

        fun onBind(data: CheckListData) {
            listName.text = data.listName
            timestamp.text = data.todoTimestamp

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(itemView, position, itemList[position])
                }
            }

            // 리스트 삭제 시
            itemView.findViewById<Button>(R.id.btn_delete).setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentItem = itemList[position]
                    // 가져온 데이터를 사용하여 deleteList 함수를 호출합니다.
                    dbTool.DeleteList(currentItem.listName!!)
                }
            }
        }
    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int, titleName: String)
        fun onItemClick(view: View, position: Int, item: CheckListData)
    }

    private var itemClickListener: ItemClickListener? = null

    fun setOnItemClickListener(listener: ItemClickListener) {
        this.itemClickListener = listener
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_check_list, parent, false)
        return CheckListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CheckListViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }

    // 초기 데이터를 로드하는 함수
    fun loadInitialData() {
        val userId = auth.currentUser?.email ?: return
        Log.d("CList","${userId}")
        firestore.collection("checklists")
            .whereArrayContains("userIds", userId)
            .get()
            .addOnSuccessListener { documents ->
                itemList.clear()
                for (document in documents) {
                    val checkList = document.toObject(CheckListData::class.java)
                    itemList.add(checkList)
                }
                notifyDataSetChanged()
            }
            .addOnFailureListener {
                // 데이터 읽기 실패 시 처리

            }
    }

    // 새로운 데이터를 추가하는 함수
    fun addData(newData: CheckListData) {
        itemList.add(newData)
        notifyItemInserted(itemList.size - 1)
    }

    // 기존 데이터와 새로운 데이터 목록을 업데이트하는 함수
    fun update(newList: ArrayList<CheckListData>) {
        itemList = newList
        notifyDataSetChanged()
    }
}
