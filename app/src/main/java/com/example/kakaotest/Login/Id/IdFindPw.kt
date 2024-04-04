package com.example.kakaotest.Login.Id

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.R
import com.example.kakaotest.Utility.dialog.AlertDialogHelper
import com.google.firebase.firestore.FirebaseFirestore

//자체 로그인 비밀번호 찾기
class IdFindPw : AppCompatActivity() {

    private var firestore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_idfind_pw)

        // 현재 액티비티를 종료하여 이전 화면으로 돌아감
        val backButton: ImageButton = findViewById(R.id.back_btn)
        backButton.setOnClickListener {
            finish()
        }

        firestore = FirebaseFirestore.getInstance()

        //자체회원가입_ 비밀번호 재설정
        val id_findpw_Btn = findViewById<Button>(R.id.id_findPW_button)
        id_findpw_Btn.setOnClickListener {
            findPW()
        }
    }

    private fun findPW() {
        val id: EditText = findViewById(R.id.id)
        val name: EditText = findViewById(R.id.name)
        val phone: EditText = findViewById(R.id.phone)

        val enteredId = id.text.toString()
        val enteredName = name.text.toString()
        val enteredPhone = phone.text.toString()

        // "user" 컬렉션에서 쿼리 생성
        val query = firestore?.collection("user")
            ?.whereEqualTo("name", enteredName)
            ?.whereEqualTo("id", enteredId)
            ?.whereEqualTo("phone", enteredPhone)

        query?.get()
            ?.addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // 입력한 정보들과 일치하는 문서가 없을 경우
                    AlertDialogHelper().showAlertMessage(
                        this,
                        "입력된 정보와 일치하는 계정이 없습니다.\n 다시 입력해주십시오.",
                        "확인",
                        null,
                        null,
                        DialogInterface.OnClickListener { dialog, which ->
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                dialog.dismiss()
                            }
                        })
                } else {
                    for (document in documents) {
                    // 입력한 정보들과 일치하는 문서가 있을 경우
                    AlertDialog.Builder(this)
                        .setMessage("입력된 정보와 일치하는 계정이 있습니다.\n 재설정 하시겠습니까?")
                        .setPositiveButton("재설정") { _, _ ->
                            val intent = Intent(this, ResetPassword::class.java)

                            intent.putExtra("documentId", document.id)
                            Toast.makeText(this, "해당 아이디가 있는 문서 ID: $document.id", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }
                        .setNegativeButton("취소", null)
                        .show()
                }
            }}
            ?.addOnFailureListener { exception ->
                // 쿼리 중 에러 발생 시
                Toast.makeText(this, "해당 아이디를 찾지 못하였습니다.: $exception", Toast.LENGTH_SHORT).show()
            }
    }


}
