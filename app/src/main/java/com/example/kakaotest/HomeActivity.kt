package com.example.kakaotest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.kakaotest.Fragment.AccountFragment
import com.example.kakaotest.Fragment.CommunityFragment
import com.example.kakaotest.Fragment.RecommendFragment
import com.example.kakaotest.Fragment.ScheduleFragment
import com.example.kakaotest.databinding.ActivityHomeBinding

private const val TAG_SCHEDULE = "schedule_fragment"
private const val TAG_COMMUNITY = "community_fragment"
private const val TAG_RECOMMEND = "reccmmend_fragment"
private const val TAG_ACCOUNT = "account_fragment"
class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 처음 화면은 startFragment로 설정
        setFragment("start_fragment", HomeFragment.newInstance("value1", "value2"))

        // 프래그먼트 인스턴스를 생성합니다.
        val HomeFragment = HomeFragment.newInstance("value1", "value2")

        // 프래그먼트를 트랜잭션을 통해 액티비티에 추가합니다.
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrameLayout, HomeFragment)
            .commit()



        // BottomNavigationView에 리스너 설정
        binding.navigationView.setOnNavigationItemSelectedListener { item ->
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

        // 모든 프래그먼트를 숨김
        for (existingFragment in manager.fragments) {
            fragTransaction.hide(existingFragment)
        }

        // 선택된 프래그먼트를 추가하거나 보이게 함
        if (manager.findFragmentByTag(tag) == null) {
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        } else {
            fragTransaction.show(fragment)
        }

        fragTransaction.commitAllowingStateLoss()






    }}
