package com.example.kakaotest.Login.Id

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.R
import com.example.kakaotest.Utility.AlertDialogHelper
import com.google.firebase.firestore.FirebaseFirestore
//자체 로그인 아이디 찾기

class IdFindId : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_id_find_id)


        // 현재 액티비티를 종료하여 이전 화면으로 돌아감
        val backButton: ImageButton = findViewById(R.id.back_btn)

        backButton.setOnClickListener {
            finish()
        }

        //자체회원가입_ 아이디 찾기
        var firestore: FirebaseFirestore? = null
        firestore = FirebaseFirestore.getInstance()



        fun findId() {
            val id_id_name: EditText = findViewById<EditText>(R.id.id_findid_name)


            val id_id_email = findViewById<EditText>(R.id.id_findid_email)
            val id_id_phone = findViewById<EditText>(R.id.id_findid_phone)


            val enteredname = id_id_name.text.toString()
            val enteredEmail = id_id_email.text.toString()
            val enteredPhone = id_id_phone.text.toString()


            // "user" 컬렉션에서 쿼리 생성
            val query = firestore.collection("user")
                .whereEqualTo("name", enteredname)
                .whereEqualTo("email", enteredEmail)
                .whereEqualTo("phone", enteredPhone)

            query.get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        // 입력한 이름과 이메일과 일치하는 문서가 없을 경우 (회원X)
                        AlertDialogHelper().showAlertMessage(this,"가입되지 않은 계정입니다.","확인",null,null)
                    } else {
                        // 입력한 이름과 이메일이 일치하는 문서가 있을 경우
                        for (document in documents) {
                            // 각 문서에서 "id" 필드 값을 가져옴
                            val id = document["id"]
                            AlertDialogHelper().showAlertMessage(this,"회원님의 아이디는\n$id 입니다.","확인",null,null)


                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // 쿼리 중 에러 발생 시
                    Toast.makeText(this, "해당 아이디를 찾지 못하였습니다.: $exception", Toast.LENGTH_SHORT)
                        .show()
                }

        }



        val id_findid_Btn = findViewById<Button>(R.id.id_findid_button)
            id_findid_Btn.setOnClickListener {
                findId()
            }


        }




    }


