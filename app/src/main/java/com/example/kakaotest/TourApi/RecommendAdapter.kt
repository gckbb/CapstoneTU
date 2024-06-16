package com.example.kakaotest.TourApi

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kakaotest.DataModel.Recommend
import com.example.kakaotest.R

class RecommendAdapter(private val recommends: List<Recommend>) :
    RecyclerView.Adapter<RecommendAdapter.RestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommend_item, parent, false)
        return RestaurantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val currentItem = recommends[position]

            // Glide를 사용하여 이미지 설정
        Glide.with(holder.itemView.context)
            .load(currentItem.firstimage2) // 이미지 URL
            .into(holder.thumbnail) // ImageView에 설정

        holder.textViewTitle.text = currentItem.title
        holder.textViewAddress.text = currentItem.addr1
        // 아이템 클릭 리스너 설정
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, RecommendDetailActivity::class.java).apply {
                // 클릭된 아이템의 상세 정보를 인텐트에 추가
                putExtra("restaurant", currentItem)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = recommends.size

    class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail) ?: throw NullPointerException("Thumbnail ImageView is null")
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle) ?: throw NullPointerException("TextViewTitle is null")
        val textViewAddress: TextView = itemView.findViewById(R.id.textViewAddress) ?: throw NullPointerException("TextViewAddress is null")
    }

}

