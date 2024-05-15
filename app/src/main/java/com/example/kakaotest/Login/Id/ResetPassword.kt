package com.example.kakaotest.Login.Id

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.Login.MainActivity
import com.example.kakaotest.R
import com.example.kakaotest.Utility.dialog.AlertDialogHelper
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
//자체 회원가입한 계정 비밀번호 재설정
class ResetPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        var firestore: FirebaseFirestore? = null
        firestore = FirebaseFirestore.getInstance()



        // Intent에서 데이터 추출
        val documentId = intent.getStringExtra("documentId")



        val newpassword: EditText = findViewById<EditText>(R.id.newpw)
        val confirmpassword: EditText = findViewById<EditText>(R.id.newpw2)

        val savebutton :Button = findViewById<Button>(R.id.savebutton)
        savebutton.setOnClickListener {
        if (newpassword.text.toString() == confirmpassword.text.toString()) {

            if (documentId != null) {
                firestore.collection("user").document(documentId)
                    .update("pw", newpassword.text.toString())
                    .addOnSuccessListener {
                        Log.d(TAG, "password update successfully written!")
                        Snackbar.make(window.decorView.rootView, "새 비밀번호가 저장되었습니다.", Snackbar.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    }
                    .addOnFailureListener { e ->
                        val str: String = e.message.toString()
                        Log.d(TAG, "실패:${str}")

                    }
            }

        }else {
            AlertDialogHelper().showAlertMessage(
                this,
                "비밀번호가 일치하지 않습니다.",
                "확인",
                null,
                null,
                DialogInterface.OnClickListener { dialog, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        dialog.dismiss()
                    }
                })
        }
    }}
}