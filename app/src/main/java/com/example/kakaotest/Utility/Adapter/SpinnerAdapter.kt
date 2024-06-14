package com.example.kakaotest.Utility.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

import com.example.kakaotest.R
class SpinnerAdapter(private val context: Context, private val list: List<String>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var text: String? = null

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // 화면에 들어왔을 때 보여지는 텍스트뷰 설정
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_outer_view, parent, false)
        }
        text = list[position]
        convertView?.findViewById<TextView>(R.id.spinner_inner_text)?.text = text
        return convertView!!
    }

    // 클릭 후 나타나는 텍스트뷰 설정
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_inner_view, parent, false)
        }
        text = list[position]
        convertView?.findViewById<TextView>(R.id.spinner_text)?.text = text
        return convertView!!
    }

    // 스피너에서 선택된 아이템을 액티비티에서 꺼내오는 메서드
    fun getItemText(): String? {
        return text
    }
}