package com.example.kakaotest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.kakaotest.Fragment.AccountFragment
import com.example.kakaotest.Fragment.CommunityFragment
import com.example.kakaotest.Fragment.RecommendFragment
import com.example.kakaotest.Fragment.ScheduleFragment

import com.example.kakaotest.Login.SavedUser
import com.example.kakaotest.databinding.ActivityHomeBinding



private const val TAG_SCHEDULE = "schedule_fragment"
private const val TAG_COMMUNITY = "community_fragment"
private const val TAG_RECOMMEND = "reccmmend_fragment"
private const val TAG_ACCOUNT = "account_fragment"
private const val TAG_HOME = "home_fragment"

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = SavedUser().getUserDataFromSharedPreferences(this) //회원정보 문서 ID

        // 홈 프래그먼트를 초기 화면으로 설정
        setFragment(TAG_HOME, HomeFragment())



        binding.navigationView.setOnItemSelectedListener { item ->
            // 선택된 아이템의 아이콘을 변경하고 원하는 프래그먼트로 이동
            when (item.itemId) {
                R.id.ScheduleFragment -> {
                    setFragment(TAG_SCHEDULE, ScheduleFragment())


                }
                R.id.CommunityFragment -> {
                    setFragment(TAG_COMMUNITY, CommunityFragment())


                }
                R.id.RecommendFragment -> {
                    setFragment(TAG_RECOMMEND, RecommendFragment())


                }
                R.id.AccountFragment -> {
                    setFragment(TAG_ACCOUNT, AccountFragment())


                }
            }
            true
        }
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()



        // 현재 화면에 표시된 프래그먼트를 숨깁니다.
      //  manager.fragments.forEach {
           // fragTransaction.hide(it)
     //   }

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.replace(R.id.mainFrameLayout, fragment, tag)
        }



        fragTransaction.commitAllowingStateLoss()
    }



}


