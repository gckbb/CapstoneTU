

package com.example.kakaotest.Utility.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.kakaotest.databinding.FragmentPwAuthDialogBinding
//비밀번호 재설정 메일 전송 확인 다이얼로그
class  PwAuthDialogFragment : DialogFragment() {  // Change the class name to avoid conflicts
    private var _binding: FragmentPwAuthDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPwAuthDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        // Set the layout background to be transparent, not required


        binding.dialBtn.setOnClickListener {
            dismiss()    // 대화상자 닫는 함수
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
