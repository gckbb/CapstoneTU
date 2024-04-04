package com.example.kakaotest.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kakaotest.Map.WhereActivity
import com.example.kakaotest.databinding.FragmentHomeBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    // 버튼 클릭 콜백을 위한 인터페이스 정의
    interface ButtonClickCallback {
        fun onButtonClick()
    }

    // 콜백 인스턴스를 저장하는 변수 선언
    private var buttonClickCallback: ButtonClickCallback? = null

    // 뷰 바인딩을 위한 변수
    private var _binding: FragmentHomeBinding? = null
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // 검색 버튼 클릭 리스너 설정
        binding.searchBtn.setOnClickListener {
                val intent = Intent(requireActivity(), WhereActivity::class.java)
                startActivity(intent)
            Log.d("PLAN","homefragment -> whereactivity")
        }
        return view
    }

    // 콜백 인스턴스 설정 메서드
    fun setButtonClickCallback(callback: ButtonClickCallback) {
        this.buttonClickCallback = callback
    }

    companion object {
        /**
         * 이 팩토리 메서드를 사용하여 프래그먼트의 새 인스턴스를 생성합니다.
         *
         * @param param1 매개변수 1.
         * @param param2 매개변수 2.
         * @return HomeFragment의 새 인스턴스.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
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
