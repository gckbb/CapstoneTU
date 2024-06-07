package com.example.kakaotest.Community

import com.example.kakaotest.DataModel.CashBook.CashListData
import com.example.kakaotest.DataModel.CashBook.CashbookData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PostDB {
    private val db = Firebase.database
    private val myRef = db.getReference("community")

    //Post 추가
    /*fun AddPost(PostTitle:String, PostContent:String, PostPhoto:String? = null,TimeStamp:String, UID:String){
        val post = PostData(PostTitle, PostContent, PostPhoto, TimeStamp, UID)
        myRef.child(UID).child("mypost").child(PostTitle).setValue(post)
    }*/
    fun AddPost(PostTitle:String, PostContent:String, PostPhoto:String? = null,TimeStamp:String, UID:String){
        val post = PostData(PostTitle, PostContent, PostPhoto, TimeStamp, UID)
        myRef.child("post").child(PostTitle).setValue(post)
    }

    //Post 삭제
    fun DeletePost(PostTitle:String, UID:String){
        myRef.child(UID).child("mypost").child(PostTitle).removeValue()
    }

}