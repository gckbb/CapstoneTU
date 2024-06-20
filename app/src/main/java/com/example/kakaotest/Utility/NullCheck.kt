package com.example.kakaotest.Utility

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextUtils
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.kakaotest.R
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import com.example.kakaotest.DataModel.TravelPlan
import org.w3c.dom.Text

private var plan = TravelPlan()
private val travelPlanManager = TravelPlanManager()
object NullCheck {
    fun checkAndChangeEditText(editText: EditText) {
        if (TextUtils.isEmpty(editText.text.toString())) {
            // Change the background color to red if EditText is empty
            editText.background = ColorDrawable(Color.RED)

        }
    }




    fun changeTextColor(buttons : List<Button>,text : TextView){

        var anyButtonSelected = buttons.any { it.isSelected }
        if (!anyButtonSelected) {
            // 버튼 중 하나도 선택되지 않았을 때 텍스트 색상 변경
            text.setTextColor(Color.RED)
        }
    }
}
