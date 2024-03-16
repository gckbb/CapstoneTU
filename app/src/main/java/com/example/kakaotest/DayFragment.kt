package com.example.kakaotest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import com.example.kakaotest.Plan.DRouteData

class DayFragment : ListFragment() {

    private lateinit var routeListAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_day, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val listView = listView
        routeListAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = routeListAdapter
    }

    fun updateListView(routeList: List<String>) {
        routeListAdapter.clear()
        routeListAdapter.addAll(routeList)
        routeListAdapter.notifyDataSetChanged()
    }
}
