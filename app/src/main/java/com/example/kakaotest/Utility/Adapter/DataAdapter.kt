import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.kakaotest.DataModel.tmap.SearchData
import com.example.kakaotest.R
import com.example.kakaotest.Utility.Adapter.SelectRecyclerAdapter
import com.example.kakaotest.Utility.dialog.AlertDialogHelper
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
class DataAdapter(
    context: Context,
    private val resourceId: Int,
    val list: ArrayList<SearchData>,
    private val listBtnClickListener: ListBtnClickListener,
) : ArrayAdapter<SearchData>(context, resourceId, list), View.OnClickListener {

    private val selectedPlacesList = ArrayList<SearchData>()

    var selectRecyclerAdapter: SelectRecyclerAdapter? = null // SelectRecyclerAdapter 프로퍼티 추가
    interface ListBtnClickListener {
        fun onListBtnClick(position: Int, selectedPlacesList: ArrayList<SearchData>)
        fun onItemClick(item: SearchData)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView: View = convertView ?: LayoutInflater.from(context).inflate(resourceId, parent, false)

        val dataphoto = itemView.findViewById<ImageView>(R.id.data_image)
        val dataname = itemView.findViewById<TextView>(R.id.data_name)
        val dataaddress = itemView.findViewById<TextView>(R.id.data_place)
        val selectBtn = itemView.findViewById<Button>(R.id.selectBtn)

        val data = list[position]
        val resourceId = context.resources.getIdentifier("point", "drawable", context.packageName)
        dataphoto.setImageResource(resourceId)
        dataname.text = data.id
        dataaddress.text = data.address

        selectBtn.tag = position
        selectBtn.setOnClickListener(this)


        selectBtn.setBackgroundResource(R.drawable.buttonshape4)


        return itemView
    }

    override fun onClick(v: View) {
        val position = v.tag as Int
        val data = list[position]

        // 선택된 항목에 대한 처리만 남김
        if (!selectedPlacesList.contains(data)) {
            selectedPlacesList.add(data)
            Toast.makeText(context, "${data.id} 추가", Toast.LENGTH_SHORT).show()
            (v as Button).setBackgroundResource(R.drawable.buttonshape2)
            Log.d("placeadd",selectedPlacesList.toString())
            selectRecyclerAdapter?.notifyDataSetChanged()
        } else{
            Toast.makeText(context, "이미 추가된 장소입니다.", Toast.LENGTH_SHORT).show()
        }

        // 삭제 기능 관련 코드는 모두 제거

        listBtnClickListener.onListBtnClick(position, selectedPlacesList)
        notifyDataSetChanged()
    }

    fun getClickedItems(): List<SearchData> {
        return selectedPlacesList
    }

    fun getItemCount(): Int = list.size
    private fun deleteItem(position: Int) {
        try {
            val data = selectedPlacesList[position]
            selectedPlacesList.removeAt(position)
            notifyDataSetChanged()
            Toast.makeText(context, "${data.id} 삭제", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
