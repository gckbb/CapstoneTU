package com.example.kakaotest.Utility

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.Login.SavedUser

class DestroyService : Service() {
    val userdata = SavedUser()
    private val TAG = javaClass.simpleName
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand()")

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        Log.e(TAG, "onTaskRemoved()")
        Log.e(TAG, "This task removed from task list!")
        userdata.clearUserDataFromSharedPreferences(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        userdata.clearUserDataFromSharedPreferences(this)
        Log.e(TAG, "onDestroy()")

    }
}