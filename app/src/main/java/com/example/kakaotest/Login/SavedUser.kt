package com.example.kakaotest.Login

import android.content.Context

class SavedUser {

    // SharedPreferences에 사용자 정보(documentID) 저장
    fun saveUserDataToSharedPreferences(context: Context, documentId: String ) {
        val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)  //user_data는 SharedPreferences의 이름, 해당 앱에서만 접근 가능
        val editor = sharedPreferences.edit()
        editor.putString("document_id", documentId)
        editor.apply()
    }

    // 어느 화면에서든 사용자 정보를 SharedPreferences에서 가져오기
    fun getUserDataFromSharedPreferences(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        return sharedPreferences.getString("document_id", null)
    }

    // 로그아웃 시 사용자 정보를 SharedPreferences에서 제거
    fun clearUserDataFromSharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("document_id")
        editor.apply()
    }
}