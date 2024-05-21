package com.example.kakaotest.Utility.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.Map.WhereActivity
import com.example.kakaotest.R
import com.example.kakaotest.search.SearchResultEntity
import com.example.kakaotest.databinding.ViewholderSearchResultItemBinding

class SearchRecyclerAdapter: RecyclerView.Adapter<SearchRecyclerAdapter.SearchResultItemViewHolder>() {

    private var searchResultList: List<SearchResultEntity> = listOf()
    private lateinit var searchResultClickListener: (SearchResultEntity) -> Unit

    class SearchResultItemViewHolder(private val binding:ViewholderSearchResultItemBinding, val searchResultClickListener: (SearchResultEntity) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: SearchResultEntity) = with(binding) {
            textTextView.text = data.name
            subTextTextView.text = data.address
        }

        fun bindViews(data: SearchResultEntity) {
            binding.selectBtn.setOnClickListener {
                searchResultClickListener(data)
             //   binding.selectBtn.setBackgroundColor(Color.YELLOW)
                binding.selectBtn.setBackgroundResource(R.drawable.buttonshape2)
            }

      //      binding.root.setOnClickListener {
             //   searchResultClickListener(data)
              //  binding.root.setBackgroundColor(Color.BLUE)

         //   }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultItemViewHolder {
        val view = ViewholderSearchResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultItemViewHolder(view, searchResultClickListener)
    }

    override fun onBindViewHolder(holder: SearchResultItemViewHolder, position: Int) {
        holder.bindData(searchResultList[position])
        holder.bindViews(searchResultList[position])
    }

    override fun getItemCount(): Int = searchResultList.size

    fun setSearchResultList(searchResultList: List<SearchResultEntity>, searchResultClickListener: (SearchResultEntity)->Unit){
        this.searchResultList = searchResultList
        this.searchResultClickListener = searchResultClickListener
        notifyDataSetChanged()
    }





}