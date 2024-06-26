package com.example.kakaotest.Utility.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.DataModel.CashBook.CashListData
import com.example.kakaotest.DataModel.CashBook.CashbookDB
import com.example.kakaotest.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CashListAdapter(private var itemList: ArrayList<CashListData>): RecyclerView.Adapter<CashListAdapter.CashListViewHolder>() {
//가계부 첫 메인화면 리스트 어댑터
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = database.reference.child("cashbook")
    val dbTool = CashbookDB()

    //가계부 목록 화면 adapter임
    init {
        // Firebase Realtime Database에서 데이터를 가져와서 itemList에 추가
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                for (itemSnapshot in snapshot.children) {
                    val listName = itemSnapshot.child("listName").getValue(String::class.java)
                    val todoTimestamp =
                        itemSnapshot.child("todoTimestamp").getValue(String::class.java)
                    listName?.let { name ->
                        todoTimestamp?.let { timestamp ->
                            itemList.add(CashListData(name, timestamp))
                        }
                    }
                }
                notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // 데이터 읽기 실패 시 처리
            }
        })
    }

    inner class CashListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var listName = itemView.findViewById<TextView>(R.id.tvTodoItem)
        var timestamp = itemView.findViewById<TextView>(R.id.tvTimeStamp)

        fun onBind(data: CashListData){
            listName.text = data.listName
            timestamp.text = data.todoTimestamp

            itemView.setOnClickListener{
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    itemClickListener?.onItemClick(itemView, position, itemList[position])
                }
            }

            //리스트 삭제 시
            itemView.findViewById<Button>(R.id.btn_delete).setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    val currentItem = itemList[position]
                    // 가져온 데이터를 사용하여 deleteList 함수를 호출
                    dbTool.DeleteList(currentItem.listName!!)
                }
            }
        }
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, titleName:String)

        fun onItemClick(view: View, position: Int, item: CashListData)
    }

    private var itemClickListener: ItemClickListener? = null

    fun setOnItemClickListener(listener: ItemClickListener){
        this.itemClickListener = listener
    }

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CashListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_check_list, parent, false)
        return CashListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    override fun onBindViewHolder(holder: CashListViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }

    fun update(newList: ArrayList<CashListData>){
        itemList = newList
        notifyDataSetChanged()
    }

}