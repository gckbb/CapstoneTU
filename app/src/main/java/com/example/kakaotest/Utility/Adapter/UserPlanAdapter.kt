package com.example.kakaotest.Utility.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.kakaotest.DataModel.ScheduleData
import com.example.kakaotest.R
import kotlinx.coroutines.Deferred
import java.util.ArrayList

class UserPlanAdapter(val context: Context, val planList: ArrayList<ScheduleData>) : BaseAdapter() {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_user_plan, null)

        /* 위에서 생성된 view를 res-layout-main_lv_item.xml 파일의 각 View와 연결하는 과정이다. */

        val date = view.findViewById<TextView>(R.id.Date)
        val time = view.findViewById<TextView>(R.id.time)
        val plantime = view.findViewById<TextView>(R.id.routetime)
        val start = view.findViewById<TextView>(R.id.startpoint)
        val type = view.findViewById<TextView>(R.id.type)
        val plandata = planList[position]


        date.text = "${plandata.startday?.date}~${plandata.endday?.date}"
        time.text = "소요시간 :"
        plantime.text = "${plandata!!.timedata!![0]/3600}시간 ${plandata!!.timedata!![0]%3600/60}분 / ${plandata!!.timedata!![1]/3600}시간 ${plandata!!.timedata!![1]%3600/60}분"
        start.text = plandata.placenamelist?.get(0)
        type.text = plandata.type




        return view
    }

    override fun getCount(): Int {
        return planList.size
    }

    override fun getItem(position: Int): Any {
        return planList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }


}