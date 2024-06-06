package com.example.kakaotest.CheckList

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kakaotest.DataModel.CheckList.CheckListData
import com.example.kakaotest.R
import com.example.kakaotest.databinding.FragmentOptionSelectBinding
import java.text.SimpleDateFormat
import android.content.Context
import android.content.Intent
import com.example.kakaotest.CashBook.CashBookActivity
import java.util.Locale

class OptionSelectFragment : Fragment() {
    private var _binding: FragmentOptionSelectBinding? = null
    private val binding get() = _binding!!
    private var sNum = 0
    private var listener: OnListSelectListener? = null


    private var dataPassListener: DataPassListener? = null

    interface OnListSelectListener {
        fun onListSelected(listTitle: String, currentDate: String, sNum: Int)
    }

    companion object {
        const val REQUEST_KEY = "OptionSelectFragment"
        fun newInstance(type: String): OptionSelectFragment {
            val fragment = OptionSelectFragment()
            val args = Bundle().apply { putString("type", type) }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOptionSelectBinding.inflate(inflater, container, false)

        val type = arguments?.getString("type")


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = arguments?.getString("type")
        binding.toggle.setOnClickListener {
            if (binding.toggle.isChecked) {
                binding.rbGroup.visibility = View.VISIBLE
                binding.rbGroup.setOnCheckedChangeListener { group, checkedID ->
                    when (checkedID) {
                        R.id.rb_1 -> {
                            Toast.makeText(requireContext(), "1선택", Toast.LENGTH_SHORT).show()
                            sNum = 1
                        }

                        R.id.rb_2 -> {
                            Toast.makeText(requireContext(), "2선택", Toast.LENGTH_SHORT).show()
                            sNum = 2
                        }

                        R.id.rb_3 -> {
                            Toast.makeText(requireContext(), "3선택", Toast.LENGTH_SHORT).show()
                            sNum = 3
                        }

                        R.id.rb_4 -> {
                            Toast.makeText(requireContext(), "4선택", Toast.LENGTH_SHORT).show()
                            sNum = 4
                        }

                        R.id.rb_5 -> {
                            Toast.makeText(requireContext(), "5선택", Toast.LENGTH_SHORT).show()
                            sNum = 5
                        }
                    }
                }
            } else {
                binding.rbGroup.visibility = View.GONE
            }
        }

        // 버튼 클릭 리스너 설정
        binding.okbtn.setOnClickListener {
            // 체크리스트 목록 이름, 생성 일자, 분류 초기 설정
            val listTitle = binding.etTitle.text.toString()
            val currentDate = SimpleDateFormat(
                "yyyy-MM-dd HH:mm",
                Locale.getDefault()
            ).format(System.currentTimeMillis())


            if (listTitle.isNotEmpty()) {
                val checklist = CheckListData(listTitle, currentDate)
                dataPassListener?.onDataPassed(listTitle, currentDate, sNum)


            }
            (activity as? CheckListActivity)?.hideOverlay()
            parentFragmentManager.popBackStack()
        }


        binding.nobtn.setOnClickListener {
            (activity as? CheckListActivity)?.hideOverlay()
            parentFragmentManager.popBackStack()
        }

    }
        override fun onAttach(context: Context) {
            super.onAttach(context)
            if (context is DataPassListener) {
                dataPassListener = context
            } else {
                throw RuntimeException("$context must implement OnListSelectListener")
            }
        }

        override fun onDetach() {
            super.onDetach()
            listener = null
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }





}