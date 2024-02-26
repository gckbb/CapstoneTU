package com.example.kakaotest.Login.Email

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.R
import com.example.kakaotest.Utility.PwAuthDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

//이메일 비밀번호 재설정
class EmailFindPW : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_find_pw)


        lateinit var auth: FirebaseAuth
        val email_pw_email: EditText = findViewById<EditText>(R.id.findPW_email)
        val backButton: ImageButton = findViewById(R.id.back_btn)
        val pw_reset_Btn = findViewById<Button>(R.id.email_pw_reset)

        // auth 초기화
        auth = FirebaseAuth.getInstance()

        // 사용자 이메일 가져오기
        fun getEmail(): String? {
            val user = auth.currentUser
            return user?.email
        }

        fun sendEmailForPasswordUpdate() {
            val email = getEmail()
            if (email != null) {
                auth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val dialog = PwAuthDialogFragment()
                        dialog.show(supportFragmentManager, "CustomDialog")


                    } else {
                        Snackbar.make(
                            window.decorView.rootView,
                            "이메일 발송이 실패했습니다.",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                Snackbar.make(window.decorView.rootView, "해당 이메일이 존재하지 않습니다.", Snackbar.LENGTH_LONG).show()
            }
        }




        pw_reset_Btn.setOnClickListener {
            sendEmailForPasswordUpdate()
        }


        backButton.setOnClickListener {
            // 현재 액티비티를 종료하여 이전 화면으로 돌아감
            finish()
        }



    }





}