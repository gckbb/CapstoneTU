package com.example.kakaotest.Utility.Adapter

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.DataModel.tmap.SearchData
import com.example.kakaotest.R
import com.example.kakaotest.Utility.dialog.AlertDialogHelper

class SelectRecyclerAdapter(
    private val context: Context,

    private var itemList: ArrayList<SearchData>,
    private var selectedPlacesList: ArrayList<SearchData>,
    internal var onDeleteListener: (Int) -> Unit // 삭제 이벤트 콜백 추가

) : RecyclerView.Adapter<SelectRecyclerAdapter.SelectedPlaceViewHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedPlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.selected_place_item_list, parent, false)
        return SelectedPlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectedPlaceViewHolder, position: Int) {
        val data = itemList[position]
        holder.placename.text = data.id
        holder.delete.tag = position
        holder.delete.setOnClickListener(this)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class SelectedPlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placename: TextView = itemView.findViewById(R.id.placename)
        val delete: ImageButton = itemView.findViewById(R.id.delete)
    }

    override fun onClick(view: View) {
        val position = view.tag as Int
        val data = itemList[position]
        if (selectedPlacesList.contains(data)) {
            AlertDialogHelper().showAlertMessage(context, "\n선택 취소하시겠습니까?", "네", "아니요", null,
                DialogInterface.OnClickListener { dialog, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        removeItem(data)
                        Toast.makeText(context, "${data.id} 삭제", Toast.LENGTH_SHORT).show()
                        Log.d("placedelete",selectedPlacesList.toString())
                        onDeleteListener(position) // 삭제 콜백 호출
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        dialog.dismiss()
                    }
                })
        }
    }


    fun removeItem(item: SearchData) {
        val position = itemList.indexOf(item)
        if (position != -1) {
            itemList.remove(item)
            notifyItemRemoved(position)
        }
    }



    fun addItems(items: List<SearchData>) {
        itemList.addAll(items)
        notifyDataSetChanged()
    }



}
