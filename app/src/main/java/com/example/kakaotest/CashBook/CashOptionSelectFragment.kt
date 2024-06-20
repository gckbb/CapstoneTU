package com.example.kakaotest.CashBook

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kakaotest.CheckList.DataPassListener
import com.example.kakaotest.CheckList.OptionSelectFragment
import com.example.kakaotest.DataModel.CashBook.CashListData
import com.example.kakaotest.DataModel.CheckList.CheckListData
import com.example.kakaotest.R
import com.example.kakaotest.databinding.FragmentCashOptionSelectBinding
import java.text.SimpleDateFormat
import java.util.Locale


class CashOptionSelectFragment : Fragment() {
  private var _binding : FragmentCashOptionSelectBinding?=null

    private val binding get() = _binding!!
    private var sNum=0
    private var listener : OnListSelectListener?=null

    private var dataPassListener : DataPassListener? = null

    interface OnListSelectListener {
        fun onListSelected(listTitle: String, currentDate: String, sNum: Int)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCashOptionSelectBinding.inflate(inflater,container,false)
        val type = arguments?.getString("type")

        return binding.root

    }

    companion object {
        const val REQUEST_KEY = "CashOptionSelectFragment"
        fun newInstance(type: String): CashOptionSelectFragment {
            val fragment = CashOptionSelectFragment()
            val args = Bundle().apply { putString("type", type) }
            fragment.arguments = args
            return fragment
        }
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
                    }
                }
            } else {
                binding.rbGroup.visibility = View.GONE
            }
        }
        // 버튼 클릭 리스너 설정
        binding.lsBtn.setOnClickListener {
            // 체크리스트 목록 이름, 생성 일자, 분류 초기 설정
            val listTitle = binding.etTitle.text.toString()
            val currentDate = SimpleDateFormat(
                "yyyy-MM-dd HH:mm",
                Locale.getDefault()
            ).format(System.currentTimeMillis())


            if (listTitle.isNotEmpty()) {
                val checklist = CashListData(listTitle, currentDate)
                dataPassListener?.onDataPassed(listTitle, currentDate, sNum)


            }
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