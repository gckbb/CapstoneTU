package com.example.kakaotest.Utility.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaotest.DataModel.selectItem
import com.example.kakaotest.DataModel.tmap.SearchData
import com.example.kakaotest.R

class SelectedPlacesAdapter(val context: Context, val selectList:ArrayList<String>): BaseAdapter() {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.selected_place_item_list, null)

        /* 위에서 생성된 view를 res-layout-main_lv_item.xml 파일의 각 View와 연결하는 과정이다. */
       // val dataphoto = view.findViewById<ImageView>(R.id.placeImage)
        val dataname = view.findViewById<TextView>(R.id.placeName)



        val data = selectList[position]
     //   val resourceId = context.resources.getIdentifier("point", "drawable", context.packageName)
     //   dataphoto.setImageResource(resourceId)
        dataname.text = data

        return view
    }
    override fun getCount(): Int {
        return selectList.size
    }

    override fun getItem(position: Int): Any {
        return selectList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }




}


/*(
    private val selectedItems: List<String> // 선택된 아이템의 ID 리스트
) : RecyclerView.Adapter<SelectedPlacesAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // 뷰 홀더에 필요한 뷰 바인딩
        val textView: TextView = view.findViewById(R.id.textView)
    }
    fun updateSelectedItems(newSelectedItems: List<String>) {
        val data = dataList[position]
        displayList = allItems.filter { newSelectedItems.contains(it.id) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.selected_place_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemId = selectedItems[position] // 선택된 아이템의 ID 가져오기
        val item = allItems.firstOrNull { it.id == itemId } // 전체 리스트에서 해당 ID를 가진 아이템 찾기

        if (item != null) {
            // 아이템 정보를 ViewHolder에 바인딩
            holder.textView.text = item.id // 예시로 이름을 설정함
        }
    }

    override fun getItemCount(): Int {
        return selectedItems.size
    }


}*/
