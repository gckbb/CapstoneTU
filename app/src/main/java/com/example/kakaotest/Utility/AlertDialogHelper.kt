package com.example.kakaotest.Utility


import android.content.Context
import android.content.DialogInterface
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
        NeutralBtn: String?,
        listener: DialogInterface.OnClickListener?
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
            builder.setPositiveButton(PositiveBtn) { dialog, which ->
                Log.d("MyTag", "positive")
                listener?.onClick(dialog, which)
            }
        }

        // Negative 버튼 추가
        if (NegativeBtn != null) {
            builder.setNegativeButton(NegativeBtn) { dialog, which ->
                Log.d("MyTag", "negative")
                listener?.onClick(dialog, which)
            }
        }

        // Neutral 버튼 추가
        if (NeutralBtn != null) {
            builder.setNeutralButton(NeutralBtn) { dialog, which ->
                Log.d("MyTag", "neutral")
                listener?.onClick(dialog, which)
            }
        }

        // AlertDialog 생성 및 표시
        builder.create().show()
    }
}


