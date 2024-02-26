package com.example.kakaotest.Utility


import android.content.Context
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AlertDialog



/*
* 사용방법
*  AlertDialogHelper().showAlertMessage(this,"msg","text",null,null) 등등
* */
class AlertDialogHelper {
    fun showAlertMessage(
        context: Context,
        message: String,
        PositiveBtn: String?,
        NegativeBtn: String?,
        NeutralBtn: String?
    ) {
        val builder = AlertDialog.Builder(context)

        // TextView 생성
        val messageTextView = TextView(context)
        messageTextView.text = message
        messageTextView.gravity = Gravity.CENTER
        messageTextView.textSize = 18f

        // TextView를 AlertDialog에 추가
        builder.setView(messageTextView)

        // Positive 버튼 추가
        if (PositiveBtn != null) {
            builder.setPositiveButton(PositiveBtn) { dialog, _ ->
                Log.d("MyTag", "positive")
                dialog.dismiss()
            }
        }

        // Negative 버튼 추가
        if (NegativeBtn != null) {
            builder.setNegativeButton(NegativeBtn) { dialog, _ ->
                Log.d("MyTag", "negative")
                dialog.dismiss()
            }
        }

        // Neutral 버튼 추가
        if (NeutralBtn != null) {
            builder.setNeutralButton(NeutralBtn) { dialog, _ ->
                Log.d("MyTag", "neutral")
                dialog.dismiss()
            }
        }

        // AlertDialog 생성 및 표시
        builder.create().show()
    }
}


