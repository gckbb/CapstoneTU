package com.example.kakaotest.Map

import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import com.example.kakaotest.DataModel.ScheduleData
import com.example.kakaotest.DataModel.Weather.*
import com.example.kakaotest.DataModel.Weather.listdata
import com.example.kakaotest.HomeActivity

import com.example.kakaotest.Login.SavedUser
import com.example.kakaotest.Utility.Adapter.WeatherAdapter
import com.example.kakaotest.Utility.CoordinateConverter
import com.example.kakaotest.Utility.Database
import com.example.kakaotest.Utility.SharedPreferenceUtil
import com.example.kakaotest.Utility.WeatherObject
import com.example.kakaotest.Utility.tmap.WeatherApiAdapter
import com.example.kakaotest.databinding.ActivityWeatherBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.util.*


class WeatherActivity : AppCompatActivity() {
    private var mBinding: ActivityWeatherBinding? = null
    private val binding get() = mBinding!!
    lateinit var userdata: SavedUser
    val dbtool = Database()
    lateinit var userPlanData2 : ScheduleData
    private lateinit var userplandata : ArrayList<ScheduleData>
    val apiAdapter = WeatherApiAdapter()
    var count = 0
    var datalist = ArrayList<listdata>()
    val contextdata = this

    override fun onCreate(savedInstanceState: Bundle?) {
        userdata = SavedUser()
        super.onCreate(savedInstanceState)
        mBinding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPlanData2 = SharedPreferenceUtil.getPlanFromSharedPreferences(this)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.home.setOnClickListener {
            val intent=Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }

        setWeather(userPlanData2.londata!![0],userPlanData2.latdata!![0])
/*
        lifecycleScope.launch {
            try {
                weatherstart()
            } catch (e: Exception) {
                Log.e("weatherapi", "Error: ${e.message}", e)
            }
        }

 */






    }
    private fun setWeather(lon:Double,lat:Double) {
        val xydata = CoordinateConverter().convertToXy(lat,lon)
        val call = WeatherObject.getRetrofitService().getWeather(1000, 1, "JSON", "20240918", "0500", xydata.nx, xydata.ny)
        call.enqueue(object : retrofit2.Callback<WEATHER> {
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if(response.isSuccessful) {
                    Log.d("weather","good")
                    val items = response.body()?.response?.body?.items!!
                    lateinit var datatemp : String
                    for(i in items.item){
                        if(i.category == "TMP"){
                            datatemp = i.fcstValue
                        }
                        else if(i.category == "POP") {
                            datalist.add(listdata(i.fcstTime,i.fcstValue,i.fcstDate,datatemp))
                        }

                    }
                    val Adapter = WeatherAdapter(contextdata,datalist)
                    binding.planlistview.adapter = Adapter
                }

            }
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }

        })
    }
    suspend fun weatherstart() {
        coroutineScope {
            val deferredresult = async(Dispatchers.IO) {
                apiAdapter.apiRequest(userPlanData2.latdata?.get(0)!!, userPlanData2.londata?.get(0)!! )
            }
            val items = deferredresult.await()!!.body.items
            while(true) {
                if(count < items.item.size){
                    datalist.add(listdata(items.item[count].fcstTime,items.item[count+7].fcstValue,items.item[count].fcstDate,items.item[count].fcstValue))
                    count = count + 12
                }
                else {
                    break
                }
            }
            val Adapter = WeatherAdapter(contextdata,datalist)
            binding.planlistview.adapter = Adapter
        }


    }



}




