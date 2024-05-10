package com.example.kakaotest.TourApi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.DataModel.AreaData
import com.example.kakaotest.R


class AreaBasedAdapter(private val areadatas: List<AreaData>) :
    RecyclerView.Adapter<AreaBasedAdapter.AreaDataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaDataViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.areadata_item, parent, false)
        return AreaDataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AreaDataViewHolder, position: Int) {
        val currentItem = areadatas[position]
        holder.textViewTitle.text = currentItem.title
        holder.textViewAddress.text = currentItem.addr1
    }

    override fun getItemCount() = areadatas.size

    class AreaDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewAddress: TextView = itemView.findViewById(R.id.textViewAddress)
    }
}