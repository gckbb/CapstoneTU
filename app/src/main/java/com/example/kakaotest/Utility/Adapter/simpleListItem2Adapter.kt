package com.example.kakaotest.Utility.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.example.kakaotest.R

class simpleListItem2Adapter(
    context: Context,
    private var items: MutableList<SelectedPlaceData>
) : ArrayAdapter<SelectedPlaceData>(context, R.layout.item_selected_place, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_selected_place, parent, false)

        val item = getItem(position)
        val text1 = view.findViewById<TextView>(R.id.place_name)
        val text2 = view.findViewById<TextView>(R.id.addr)

        text1.text = item?.placeName
        text2.text = item?.address

        return view
    }

    // 데이터가 변경되었을 때 items 리스트를 업데이트하는 메서드 추가
    fun updateData(newItems: MutableList<SelectedPlaceData>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}