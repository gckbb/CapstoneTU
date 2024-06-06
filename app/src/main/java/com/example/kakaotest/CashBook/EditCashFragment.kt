package com.example.kakaotest.CashBook

import OnDataPassedListener
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.kakaotest.CheckList.DataPassListener
import com.example.kakaotest.DataModel.CashBook.CashbookData
import com.example.kakaotest.R
import com.example.kakaotest.databinding.FragmentCashOptionSelectBinding
import com.example.kakaotest.databinding.FragmentEditCashBinding
import com.example.kakaotest.databinding.FragmentOptionSelectBinding

class EditCashFragment : Fragment() {
    private lateinit var item: CashbookData
    private lateinit var editTitle: EditText
    private lateinit var editContent: EditText
    private lateinit var saveButton: Button

    companion object {
        // 프래그먼트 인스턴스 생성을 위한 정적 메서드입니다.
        fun newInstance(item: CashbookData): EditCashFragment {
            val fragment = EditCashFragment()
            val args = Bundle()
            args.putSerializable("item", item)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 레이아웃을 inflate하여 프래그먼트의 뷰를 설정합니다.
        val view = inflater.inflate(R.layout.fragment_edit_cash, container, false)

        // EditText 및 Button 요소를 초기화합니다.
        editTitle = view.findViewById(R.id.editTextText)
        editContent = view.findViewById(R.id.editTextText2)
        saveButton = view.findViewById(R.id.okbtn)

        // 전달된 아이템을 가져옵니다.
        item = arguments?.getSerializable("item") as CashbookData
        // EditText에 아이템의 정보를 설정합니다.
        editTitle.setText(item.itemName)
        editContent.setText(item.itemCost.toString())

        // 저장 버튼 클릭 리스너를 설정합니다.
        saveButton.setOnClickListener {
            // EditText에서 새로운 제목 및 비용을 가져옵니다.
            val newTitle = editTitle.text.toString()
            val newContent = editContent.text.toString().toInt()
          //수정된 내용을 다시 액티비티로 전달.
            val listener = activity as? OnDataPassedListener
            listener?.onDataPassed(CashbookData(newTitle, newContent, false))
            // 여기에서는 수정 후 프래그먼트를 닫습니다.
            activity?.supportFragmentManager?.popBackStack()
        }

        return view
    }
}


