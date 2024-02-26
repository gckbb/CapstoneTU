package com.example.kakaotest.Login.Email

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.HomeActivity
import com.example.kakaotest.Login.SavedUser
import com.example.kakaotest.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//이메일 로그인 화면
class EmailLogin : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_login)

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val id: EditText = findViewById(R.id.email_edittext)
        val password: EditText = findViewById(R.id.emailPw_edittext)
        val signBtn = findViewById<Button>(R.id.email_login_button) //로그인 버튼
        val signupBtn = findViewById<TextView>(R.id.emailsignupbtn) //회원가입 버튼
        val findpasswordBtn = findViewById<TextView>(R.id.em_FindPW) //비번 찾기 버튼
        val backButton: ImageButton = findViewById(R.id.back_btn)

        //회원가입
        signupBtn.setOnClickListener {
            val intent = Intent(this, Emailsignup::class.java)
            startActivity(intent)
        }


        //비밀번호 찾기
        findpasswordBtn.setOnClickListener {
            val intent = Intent(this, EmailFindPW::class.java)
            startActivity(intent)
        }



        // Firebase Firestore 초기화
        var firestore: FirebaseFirestore? = null
        firestore = FirebaseFirestore.getInstance()

        fun saveduser(email:String){
            // "user" 컬렉션에서 쿼리 생성
            val query = firestore.collection("user")
                .whereEqualTo("email", email)

            query.get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        // 사용자 아이디와 일치하는 문서가 있을 경우
                        for (document in documents) {
                            val savedPassword = document.getString("pw")
                            if (savedPassword == password.text.toString()) {
                                SavedUser().saveUserDataToSharedPreferences(this, document.id)
                                Toast.makeText(this, "저장된 document Id = $document.id", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
        }


        //이메일 로그인
        signBtn.setOnClickListener {


            auth!!.signInWithEmailAndPassword(id.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        saveduser(id.text.toString())

                        Snackbar.make(window.decorView.rootView, "이메일 로그인에 성공하였습니다..", Snackbar.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Snackbar.make(window.decorView.rootView, "이메일 로그인에 실패하였습니다. 다시 입력해주십시오.", Snackbar.LENGTH_SHORT).show()

                    }
                }

        }




        // 현재 액티비티를 종료하여 이전 화면으로 돌아감
        backButton.setOnClickListener {
            finish()
        }


    }
}





