package com.example.kakaotest.CheckList

import android.app.Application

class ApplicationClass: Application() {

    override fun onCreate() {
        super.onCreate()

        TodoRepository.initialize(this)
    }
}