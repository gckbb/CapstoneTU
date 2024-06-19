package com.example.kakaotest.Utility.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.kakaotest.DataModel.metaRoute.Legs
import com.example.kakaotest.DataModel.metaRoute.SearchMetaData
import com.example.kakaotest.R

class MetaListAdapter (val context: Context, val routeList: List<Legs>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_meta_route, null)

        /* 위에서 생성된 view를 res-layout-main_lv_item.xml 파일의 각 View와 연결하는 과정이다. */
        val modeimage = view.findViewById<ImageView>(R.id.modeimage)
        val mode = view.findViewById<TextView>(R.id.mode)
        val time = view.findViewById<TextView>(R.id.time)
        val start = view.findViewById<TextView>(R.id.start)
        val startStation = view.findViewById<TextView>(R.id.startstation)
        val end = view.findViewById<TextView>(R.id.end)
        val endStation = view.findViewById<TextView>(R.id.endstation)
        val routetime = view.findViewById<TextView>(R.id.routetime)

        val routeData = routeList[position]
        if(routeData.mode == "WALK") {
            val resourceId = context.resources.getIdentifier("walkmarker", "drawable", context.packageName)
            modeimage.setImageResource(resourceId)
            mode.text = "도보"
            time.text = "소요시간 :"
            start.text = " "
            end.text = " "
            startStation.text = " "
            endStation.text = " "
            routetime.text = "${routeData.sectionTime/60}분 ${routeData.sectionTime%60}초"
        }
        else if(routeData.mode == "BUS") {
            val resourceId = context.resources.getIdentifier("busmarker", "drawable", context.packageName)
            modeimage.setImageResource(resourceId)
            mode.text = routeData.route
            start.text = "출발정류장 :"
            time.text = "소요시간 :"
            end.text = "도착정류장 :"
            startStation.text = routeData.start?.name
            endStation.text = routeData.end?.name
            routetime.text = "${routeData.sectionTime/60}분 ${routeData.sectionTime%60}초"
        }
        else if(routeData.mode == "SUBWAY") {
            val resourceId = context.resources.getIdentifier("subwaymarker", "drawable", context.packageName)
            modeimage.setImageResource(resourceId)
            mode.text = routeData.route
            start.text = "출발정류장 :"
            time.text = "소요시간 :"
            end.text = "도착정류장 :"
            startStation.text = routeData.start?.name
            endStation.text = routeData.end?.name
            routetime.text = "${routeData.sectionTime/60}분 ${routeData.sectionTime%60}초"
        }


        return view
    }

    override fun getItem(position: Int): Any {
        return routeList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return routeList.size
    }
}