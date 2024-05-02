package com.example.kakaotest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.Fragment.AccountFragment
import com.example.kakaotest.Fragment.CommunityFragment
import com.example.kakaotest.Fragment.HomeFragment
import com.example.kakaotest.Fragment.RecommendFragment
import com.example.kakaotest.databinding.ActivityHomeBinding

private const val TAG_SCHEDULE = "schedule_fragment"
private const val TAG_COMMUNITY = "community_fragment"
private const val TAG_RECOMMEND = "reccmmend_fragment"
private const val TAG_ACCOUNT = "account_fragment"
private const val TAG_HOME = "home_fragment"
class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 처음 화면은 HomeFragment로 설정
        setFragment(TAG_HOME, HomeFragment.newInstance("value1", "value2"))


        val firstList = intent.getParcelableArrayListExtra<SearchRouteData>("firstList")
        Log.d("PLAN","firstRoute \n"+ firstList.toString())


        // BottomNavigationView에 리스너 설정
        binding.navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.CommunityFragment -> {
                    setFragment(TAG_COMMUNITY, CommunityFragment())
                }
                R.id.HomeFragment -> {
                    setFragment(TAG_HOME, HomeFragment())
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
        val addedFragment = manager.findFragmentByTag(tag)
        if (addedFragment == null) {
            // 프래그먼트가 추가되지 않은 경우, 새로운 프래그먼트를 추가
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        } else {
            // 프래그먼트가 이미 추가된 경우, 해당 프래그먼트를 보여줌
            fragTransaction.show(addedFragment)
        }

        fragTransaction.commitAllowingStateLoss()
    }
}