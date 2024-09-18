package com.example.kakaotest.Utility.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.kakaotest.DataModel.Weather.listdata
import com.example.kakaotest.R
import java.util.ArrayList

class WeatherAdapter(val context: Context, val planList: ArrayList<listdata>) : BaseAdapter() {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_weather, null)

        /* 위에서 생성된 view를 res-layout-main_lv_item.xml 파일의 각 View와 연결하는 과정이다. */
        val weatherimage = view.findViewById<ImageView>(R.id.weatherimage)
        val date = view.findViewById<TextView>(R.id.Date)
        val time = view.findViewById<TextView>(R.id.time)
        val weather = view.findViewById<TextView>(R.id.weather)
        val rain = view.findViewById<TextView>(R.id.rain)
        val plandata = planList[position]
        var weatherdata : String = "맑음"
        var datestr = plandata.date.chunked(2)
        var timestr = plandata.time.chunked(2)


        date.text = datestr[2] + "/" + datestr[3]
        time.text = timestr[0] + ":" + timestr[1]
        rain.text = "강수확률 : " + plandata.rain + "%"
        if(plandata.rain.toDouble() >= 60) {
            val resourceId = context.resources.getIdentifier("rain", "drawable", context.packageName)
            weatherimage.setImageResource(resourceId)
            weatherdata = "비"
        }
        else if(plandata.rain.toDouble() >= 30) {
            val resourceId = context.resources.getIdentifier("cloudd", "drawable", context.packageName)
            weatherimage.setImageResource(resourceId)
            weatherdata = "흐림"
        }
        else if(plandata.rain.toDouble() >= 11){
            val resourceId = context.resources.getIdentifier("cloud", "drawable", context.packageName)
            weatherimage.setImageResource(resourceId)
            weatherdata = "구름많음"
        }
        else {
            val resourceId = context.resources.getIdentifier("sunny", "drawable", context.packageName)
            weatherimage.setImageResource(resourceId)
            weatherdata = "맑음"
        }
        weather.text = weatherdata


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