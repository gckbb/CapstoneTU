package com.example.kakaotest.Utility.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.kakaotest.DataModel.metaRoute.SearchMetaData
import com.example.kakaotest.R

class RouteListAdapter(context: Context, private val data: List<SearchMetaData>) :
    ArrayAdapter<SearchMetaData>(context, R.layout.item_route_list, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_route_list, parent, false)

        val item = getItem(position)
        val text1 = view.findViewById<TextView>(R.id.pname)
        val text2 = view.findViewById<TextView>(R.id.stime)

        // 장소 이름과 체류 시간을 설정합니다.
        text1.text = item?.pointdata?.placeName ?: "Unknown Place"
        text2.text = "체류 시간: ${item?.pointdata?.stayDuration ?: 0} 시간"

        return view
    }
}
