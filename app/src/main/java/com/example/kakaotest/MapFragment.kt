package com.example.kakaotest

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.kakaotest.databinding.FragmentMapBinding
import com.example.kakaotest.databinding.FragmentSearchBinding
import com.skt.tmap.TMapView

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MapFragment : Fragment() {
    // 버튼 클릭 콜백을 위한 인터페이스 정의
    interface ButtonClickCallback {
        fun onButtonClick()
    }

    // 콜백 인스턴스를 저장하는 변수 선언
    private var buttonClickCallback: ButtonClickCallback? = null

    // 뷰 바인딩을 위한 변수
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(com.example.kakaotest.ARG_PARAM1)
            param2 = it.getString(com.example.kakaotest.ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트의 레이아웃을 인플레이트합니다.
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        // 프래그먼트 레이아웃에서 FrameLayout을 찾습니다.
        val frameLayout: FrameLayout? = view.findViewById(R.id.tmapViewContainer)

        // FrameLayout이 null이 아닌지 확인하고 작업을 수행합니다.
        frameLayout?.let {
            val tMapView = TMapView(requireContext())
            it.addView(tMapView)
            tMapView.setSKTMapApiKey("8Mi9e1fjtt8L0SrwDMyWt9rSnLCShADl5BWTm3EP")
            tMapView.setOnMapReadyListener(object : TMapView.OnMapReadyListener {
                override fun onMapReady() {
                    // 맵 로딩 완료 후 구현
                }
            })
        }

        return view
    }




    // 콜백 인스턴스 설정 메서드
    fun setButtonClickCallback(callback: MapFragment.ButtonClickCallback) {
        this.buttonClickCallback = callback
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
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