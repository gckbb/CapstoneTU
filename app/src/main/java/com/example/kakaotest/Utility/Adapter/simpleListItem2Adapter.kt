package com.example.kakaotest.Utility.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.kakaotest.DataModel.tmap.SearchData
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData

class simpleListItem2Adapter(
    context: Context,
    val list: MutableList<SelectedPlaceData>,

    ) : ArrayAdapter<SelectedPlaceData>(context, android.R.layout.simple_list_item_2, list){



    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = convertView ?: inflater.inflate(android.R.layout.simple_list_item_2, parent, false)

        val item = getItem(position)
        if (item != null) {
            val text1 = view.findViewById<TextView>(android.R.id.text1)
            val text2 = view.findViewById<TextView>(android.R.id.text2)
            text1.text = item.placeName
            text2.text = item.address
        }

        return view
    }
}