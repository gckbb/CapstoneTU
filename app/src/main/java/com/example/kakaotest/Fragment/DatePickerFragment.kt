package com.example.kakaotest.Fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.kakaotest.R
import java.util.Calendar

class DatePickerFragment(private val onDateSelected: (year: Int, month: Int, day: Int) -> Unit) : DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 현재 날짜로 DatePickerDialog를 생성합니다.
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dialog = DatePickerDialog(requireActivity(), R.style.CreateProfileTheme, this, year, month, day)
        return dialog
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // 선택된 날짜를 콜백 함수를 통해 전달합니다.
        onDateSelected(year, month, dayOfMonth)
    }
}
