package com.example.kakaotest.Map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView

import androidx.appcompat.app.AppCompatActivity
import com.example.kakaotest.CashBook.CashBookActivity

import com.example.kakaotest.DataModel.Date
import com.example.kakaotest.DataModel.ScheduleData
import com.example.kakaotest.DataModel.metaRoute.MetaDayRoute
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.HomeActivity

import com.example.kakaotest.Login.MainActivity
import com.example.kakaotest.Login.SavedUser
import com.example.kakaotest.Utility.Adapter.UserPlanAdapter
import com.example.kakaotest.Utility.Database
import com.example.kakaotest.Utility.SharedPreferenceUtil
import com.example.kakaotest.Utility.dialog.AlertDialogHelper
import com.example.kakaotest.databinding.ActivityUserPlanBinding
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*


class UserPlanActivity : AppCompatActivity() {
    private var mBinding: ActivityUserPlanBinding? = null
    private val binding get() = mBinding!!
    lateinit var userdata: SavedUser
    val dbtool = Database()
    private lateinit var userplandata : ArrayList<ScheduleData>

    override fun onCreate(savedInstanceState: Bundle?) {
        userdata = SavedUser()
        super.onCreate(savedInstanceState)
        mBinding = ActivityUserPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.home.setOnClickListener {
            val intent=Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }


        if (userdata.getUserIdFromSharedPreferences(this) == null) {
            Log.d("ERROR", "로그인이 되어있지않음")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else {
            val userid = userdata.getUserIdFromSharedPreferences(this)!!
            test1(this, userid)
        }
        binding.planlistview.onItemClickListener = AdapterView.OnItemClickListener {
                parent,
                view,
                position,
                id -> val selectItem = parent.getItemAtPosition(position) as ScheduleData
            SharedPreferenceUtil.savePlanToSharedPreferences(this,selectItem)
            Log.d("testmsg",selectItem.toString())
            val intent = Intent(this, PlanActivity::class.java)
            startActivity(intent)

        }


    }

    fun test1(context : Context, userid: String) {

        CoroutineScope(Dispatchers.Main).launch{
            userplandata = test2(userid).await()
            val Adapter = UserPlanAdapter(context,userplandata)
            binding.planlistview.adapter = Adapter
        }

    }

    suspend fun test2(userid : String) : Deferred<ArrayList<ScheduleData>>{
        val dbtool = Database()

        val plandata2 : Deferred<ArrayList<ScheduleData>> = CoroutineScope(Dispatchers.Main).async {
            var plandata = ArrayList<ScheduleData>()
            val tempdata = dbtool.getData(userid)
            for (i in tempdata.await().documents) {
                plandata.add(ScheduleData(i.get("mainId") as String?,
                    i.get("subId") as ArrayList<String>?,
                    i.get("latdata") as ArrayList<Double>,
                    i.get("londata") as ArrayList<Double>,
                    i.get("placenamelist") as ArrayList<String>,
                    i.get("type") as String,
                    i.get("startday",Date::class.java),
                    i.get("endday",Date::class.java),
                    i.get("scheduleid") as String,
                    i.get("timedata") as ArrayList<Int>,
                    i.get("dayroutedata") as ArrayList<ArrayList<SearchRouteData>?>?,
                    i.get("dayroutedata2") as ArrayList<MetaDayRoute>?
                ))
            }
            Log.d("database",tempdata.await().documents.get(0).get("dayroutedata").toString())
            Log.d("database",tempdata.await().documents.get(0).get("dayroutedata2").toString())
            plandata
        }
        return plandata2
    }
}




