package com.example.kakaotest.Community

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.DataModel.CashBook.CashListData
import com.example.kakaotest.DataModel.CashBook.CashbookDB
import com.example.kakaotest.R
import com.example.kakaotest.Utility.Adapter.CashListAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class PostAdapter(private var itemList: ArrayList<PostData>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = database.reference.child("community").child("post")
    val dbTool = CashbookDB()

    //커뮤니티 어댑터
    init {
        // Firebase Realtime Database에서 데이터를 가져와서 itemList에 추가
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                for (itemSnapshot in snapshot.children) {
                    val postTitle = itemSnapshot.child("postTitle").getValue(String::class.java)
                    val postContent = itemSnapshot.child("postContent").getValue(String::class.java)
                    val postPhoto = itemSnapshot.child("postPhoto").getValue(String::class.java)
                    val timestamp = itemSnapshot.child("timestamp").getValue(String::class.java)
                    val UID = itemSnapshot.child("uid").getValue(String::class.java)

                    /*postTitle?.let { title ->
                        postContent?.let { content ->
                            timestamp?.let { timestamp ->
                                postPhoto?.let { photo ->
                                    UID?.let { uid ->
                                        itemList.add(PostData(title, content, photo, timestamp, uid))
                                        Log.d("PostAdapter", "asdf: $itemList")
                                    }
                                }
                            }
                        }
                    }*/
                    itemList.add(PostData(postTitle, postContent, postPhoto ?: "", timestamp, UID))

                }
                notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // 데이터 읽기 실패 시 처리
            }
        })
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var Title = itemView.findViewById<TextView>(R.id.post_title)
        var Content = itemView.findViewById<TextView>(R.id.post_content)
        var timestamp = itemView.findViewById<TextView>(R.id.post_timestamp)
        var UID = itemView.findViewById<TextView>(R.id.post_uid)

        fun onBind(data: PostData) {
            Title.text = data.postTitle
            Content.text = data.postContent
            timestamp.text = data.timestamp
            UID.text = data.uid

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(itemView, position, itemList[position])
                }
            }

            /*//리스트 삭제 시
            itemView.findViewById<Button>(R.id.btn_delete).setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    val currentItem = itemList[position]
                    // 가져온 데이터를 사용하여 deleteList 함수를 호출
                    dbTool.DeleteList(currentItem.listName!!)
                }
            }*/
        }
    }

    interface ItemClickListener {

        fun onItemClick(view: View, position: Int, item: PostData)
    }

    private var itemClickListener: ItemClickListener? = null

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }

}