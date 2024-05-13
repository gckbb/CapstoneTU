package com.example.kakaotest.Plan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class RouteListAdapter(context: Context, resource: Int, objects: MutableList<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
            viewHolder = ViewHolder()
            viewHolder.textView = view.findViewById(android.R.id.text1)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val routeString = getItem(position)
        viewHolder.textView.text = routeString

        return view!!
    }

    private class ViewHolder {
        lateinit var textView: TextView
    }
}
