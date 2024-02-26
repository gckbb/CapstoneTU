package com.example.kakaotest.Login.Email

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.Login.UserData
import com.example.kakaotest.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//이메일 회원가입

class Emailsignup : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emailsignup)


        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        // UI 요소 초기화


        val sign_email: EditText = findViewById<EditText>(R.id.email_email)
        val sign_pw = findViewById<EditText>(R.id.email_pw)
        val sign_pw_check = findViewById<EditText>(R.id.emailpw_check)

        val joinBtn = findViewById<Button>(R.id.email_signup_button)




        var firestore : FirebaseFirestore? = null
        firestore = FirebaseFirestore.getInstance()








        // 이메일 회원가입
        joinBtn.setOnClickListener {

            // email, pwd를 받아오기
            //첫번째 방법
            val email: EditText = findViewById<EditText>(R.id.email_email)
            val password = findViewById<EditText>(R.id.email_pw)
            val phone = findViewById<EditText>(R.id.phone)
            val sign_name : EditText=findViewById<EditText>(R.id.name_edittext)


            auth!!.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Snackbar.make(window.decorView.rootView, "가입에 성공하였습니다.", Snackbar.LENGTH_SHORT).show()
                        //파이어스토어에 저장
                        var resultDTO = UserData()
                        resultDTO.email = email.text.toString()
                        resultDTO.pw = password.text.toString()
                        resultDTO.name = sign_name.text.toString()
                        resultDTO.phone = phone.text.toString()
                        firestore?.collection("user")?.document()?.set(resultDTO)
                        val intent = Intent(this, EmailLogin::class.java)
                        startActivity(intent)
                    } else {
                        Snackbar.make(window.decorView.rootView, "가입에 실패하였습니다. 다시 가입해주십시오.", Snackbar.LENGTH_SHORT).show()

                    }
                }
        }
    }





}








