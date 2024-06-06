package com.example.kakaotest.Login

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kakaotest.CashBook.CashBookActivity
import com.example.kakaotest.CheckList.CheckListActivity
import com.example.kakaotest.DataModel.metaRoute.MetaData
import com.example.kakaotest.DataModel.metaRoute.MetaRoute
import com.example.kakaotest.DataModel.tmap.SearchRouteData

import com.example.kakaotest.Login.Email.EmailLogin
import com.example.kakaotest.Login.Id.IdFindId
import com.example.kakaotest.Login.Id.IdFindPw
import com.example.kakaotest.Login.Id.IdSignup
import com.example.kakaotest.Map.MapActivity
import com.example.kakaotest.R
import com.example.kakaotest.Utility.dialog.AlertDialogHelper
import com.example.kakaotest.databinding.ActivityMainBinding
import com.example.kakaotest.HomeActivity
import com.example.kakaotest.TourApi.TourApiActivity
import com.example.kakaotest.Utility.tmap.ApiAdapter
import com.example.kakaotest.Utility.tmap.ApiAdapter2

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.security.MessageDigest
import kotlin.concurrent.thread

//첫 로그인 화면
class MainActivity : AppCompatActivity() {

    private val apiAdapter2 = ApiAdapter2()

    private lateinit var auth: FirebaseAuth

    lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit
    private lateinit var binding: ActivityMainBinding
    lateinit var googleSignInClient: GoogleSignInClient

    lateinit var imageview: ImageView
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
        }
    }


    //구글 로그인
    var googleLoginReult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            var data = result.data
            var task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken)
        }

    fun firebaseAuthWithGoogle(idToken: String?) {
        var credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential((credential)).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                moveMainPage(task.result?.user)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance() //2 FirebaseAuth의 인스턴스 초기화
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, getString(R.string.kakao_app_key))

        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //지도 생성 테스트 버튼
        binding.tmapViewbtn.setOnClickListener{
            /*
            thread{
                val metaRoute: MetaRoute? = apiAdapter2.apiRequest2(
                    126.926493082645,37.6134436427887,
                    127.126936754911,37.5004198786564
                )
                Log.d("meta","meta is work")

            }

             */

        }



        // 테스트 버튼
        binding.navi.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.checklist.setOnClickListener {
            val intent = Intent(this, CheckListActivity::class.java)
            startActivity(intent)
        }

        binding.tourApi.setOnClickListener {
            val intent = Intent(this,TourApiActivity::class.java)
            startActivity(intent)
        }

        binding.cashbook.setOnClickListener {
            val intent = Intent(this, CashBookActivity::class.java)
            startActivity(intent)
        }

        //아이디 찾기
        val findidBtn =findViewById<TextView>(R.id.FindId)
        findidBtn.setOnClickListener {
            val intent = Intent(this, IdFindId::class.java)
            startActivity(intent)
        }
        //비밀번호 찾기
        val findPwBtn =findViewById<TextView>(R.id.FindPW)
        findPwBtn.setOnClickListener {
            val intent = Intent(this, IdFindPw::class.java)
            startActivity(intent)
        }
        //자체 회원가입
        val signUpBtn =findViewById<TextView>(R.id.signupbtn)
        signUpBtn.setOnClickListener {
            val intent = Intent(this, IdSignup::class.java)
            startActivity(intent)
        }


        //이메일 로그인 및 회원가입 버튼
        val emailsignbtn =findViewById<Button>(R.id. email_login_button)
        emailsignbtn.setOnClickListener {
            val intent =Intent(this, EmailLogin::class.java)
            startActivity(intent)
        }



        // Firebase Firestore 초기화
        var firestore: FirebaseFirestore? = null
        firestore = FirebaseFirestore.getInstance()




        //아이디 로그인

        val loginBtn = findViewById<Button>(R.id.id_login_button)
        loginBtn.setOnClickListener {

            val id: EditText = findViewById<EditText>(R.id.id_edittext)
            val password = findViewById<EditText>(R.id.password_edittext)
            var enteredId:String = id.text.toString()
            var enteredPassword:String = password.text.toString()

            // "user" 컬렉션에서 쿼리 생성
            val query = firestore.collection("user")
                .whereEqualTo("id", enteredId)

            query.get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        // 사용자 아이디와 일치하는 문서가 없을 경우
                        AlertDialogHelper().showAlertMessage(this,
                            "해당 아이디가 존재하지 않습니다.",
                            "확인",
                            null,
                            null,
                            DialogInterface.OnClickListener { dialog, which ->
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    dialog.dismiss()
                                }

                            })
                    }else {
                        // 사용자 아이디와 일치하는 문서가 있을 경우
                        for (document in documents) {
                            val savedPassword = document.getString("pw")
                            if (savedPassword == enteredPassword) {
                                // 비밀번호가 일치하는 경우
                                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                                SavedUser().saveUserDataToSharedPreferences(this, document.id)
                                Toast.makeText(this, "저장된 document Id = $document.id", Toast.LENGTH_SHORT).show()
                                // 로그인 성공 후 홈 화면으로 이동
                                val nextIntent = Intent(this, MapActivity::class.java)
                                startActivity(nextIntent)
                            } else {
                                // 비밀번호가 일치하지 않는 경우
                                AlertDialogHelper().showAlertMessage(this,"비밀번호가 일치하지 않습니다.","확인",null,null,

                                    DialogInterface.OnClickListener { dialog, which ->
                                        if (which == DialogInterface.BUTTON_POSITIVE) {
                                            dialog.dismiss()
                                        }

                                    })

                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // 쿼리 중 에러 발생 시
                    Toast.makeText(this, "로그인 실패: $exception", Toast.LENGTH_SHORT).show()
                }

        }


        binding.googleSignInButton.setOnClickListener {
            signIn() //signin으로 이동
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //카카오 로그인 버튼
        binding.kakaoSignInButton.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_LONG).show()
                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    //구글 signin 화면으로 이동
    private fun signIn() {
        val i = googleSignInClient.signInIntent
        googleLoginReult.launch(i)
    }

    //로그인 activity에서 Main으로
    fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    //해시 키값 확인용
    private fun getAppKeyHash() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.e(TAG, "해시키 : $hashKey")
            }
        } catch (e: Exception) {
            Log.e(TAG, "해시키를 찾을 수 없습니다 : $e")
        }
    }



}










