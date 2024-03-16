package com.example.kakaotest.Login.Id

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.Login.MainActivity
import com.example.kakaotest.DataModel.UserData
import com.example.kakaotest.R
import com.example.kakaotest.Utility.AlertDialogHelper
import com.google.firebase.firestore.FirebaseFirestore


//자체 회원가입
class IdSignup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_id_signup)





        val id_sign_name: EditText = findViewById<EditText>(R.id.id_sign_name)
        val id_sign_id: EditText = findViewById<EditText>(R.id.id_sign_id)
        val id_sign_pw = findViewById<EditText>(R.id.id_sign_pw)
        val id_sign_email: EditText = findViewById<EditText>(R.id.id_sign_email)
        val id_sign_phone: EditText = findViewById<EditText>(R.id.id_sign_phone)
        val id_sign_pw2 = findViewById<EditText>(R.id.id_pw_check)

        val id_check_Btn = findViewById<Button>(R.id.idcheck)
        val backButton: ImageButton = findViewById(R.id.back_btn)


        var firestore : FirebaseFirestore? = null
        firestore = FirebaseFirestore.getInstance()


        // 현재 액티비티를 종료하여 이전 화면으로 돌아감
        backButton.setOnClickListener {
            finish()
        }



        id_check_Btn.setOnClickListener {
                val enteredId = id_sign_id.text.toString()
            val enteredPassword = id_sign_pw.text.toString()
                // "user" 컬렉션에서 쿼리 생성
                val query = firestore.collection("user")
                    .whereEqualTo("id", enteredId)

                query.get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            // 입력한 아이디와 일치하는 문서가 없을 경우
                            AlertDialogHelper().showAlertMessage(this,"사용하실 수 있는 아이디입니다.","확인",null,null,
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (which == DialogInterface.BUTTON_POSITIVE) {
                                        dialog.dismiss()
                                    }
                                })

                        } else {
                            // 입력한 아이디와 일치하는 문서가 있을 경우
                            AlertDialogHelper().showAlertMessage(this,"존재하는 아이디입니다.\n 다시 입력해주세요.","확인",null,null,
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (which == DialogInterface.BUTTON_POSITIVE) {
                                        dialog.dismiss()
                                    }
                                })




                        }
                    }
                    .addOnFailureListener { exception ->
                        // 쿼리 중 에러 발생 시
                        Toast.makeText(this, "로그인 실패: $exception", Toast.LENGTH_SHORT).show()
                    }

            }




        val id_sign_Btn = findViewById<Button>(R.id.id_signup_button)
        // 자체  회원가입

        id_sign_Btn.setOnClickListener {
          //  clickSignUp()
            var resultDTO = UserData()

            resultDTO.id = id_sign_id.text.toString()
            resultDTO.pw = id_sign_pw.text.toString()
            resultDTO.name = id_sign_name.text.toString()
            resultDTO.email = id_sign_email.text.toString()
            resultDTO.phone = id_sign_phone.text.toString()
            resultDTO.signtime = System.currentTimeMillis()

            firestore?.collection("user")?.document()?.set(resultDTO)
            Toast.makeText(this,"회원가입 성공", Toast.LENGTH_SHORT).show()
           Log.d(TAG, "자체 회원가입 성공")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }
}