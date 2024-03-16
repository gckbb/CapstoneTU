package com.example.kakaotest

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.core.content.ContextCompat.startActivity
import com.example.kakaotest.Map.SearchData
import com.example.kakaotest.databinding.FragmentHomeBinding
import com.example.kakaotest.databinding.FragmentSearchBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {


    // 버튼 클릭 콜백을 위한 인터페이스 정의
    interface ButtonClickCallback {
        fun onButtonClick()
    }

    // 콜백 인스턴스를 저장하는 변수 선언
    private var buttonClickCallback: ButtonClickCallback? = null

    // 뷰 바인딩을 위한 변수
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    // 프래그먼트 파라미터
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 레이아웃을 위한 인플레이션
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

    // 콜백 인스턴스 설정 메서드
    fun setButtonClickCallback(callback: SearchFragment.ButtonClickCallback) {
        this.buttonClickCallback = callback
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    // View Binding 인스턴스 정리를 위해 onDestroyView를 오버라이드
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
