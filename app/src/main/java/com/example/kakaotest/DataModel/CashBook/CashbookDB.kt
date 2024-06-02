package com.example.kakaotest.DataModel.CashBook

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CashbookDB {
    private val db = Firebase.database
    private val myRef = db.getReference("cashbook")

    //체크리스트 최초 생성 시
    fun initCheckList(listName:String, currentDate:String){
        val checklist = CashListData(listName, currentDate)
        myRef.child(listName).setValue(checklist)
    }

    //todo생성 시
    fun AddTodo(listName:String, todoTitle:String, todoContent:Int, isChecked:Boolean){
        val todolist = CashbookData(todoTitle, todoContent, isChecked)
        myRef.child(listName).child("cashbook-list").child(todoTitle).setValue(todolist)
    }

    fun UpdateChecked(listName:String, isChecked:Boolean, todoTitle:String){
        myRef.child(listName).child("cashbook-list").child(todoTitle).child("checked").setValue(isChecked)
    }

    //목록 삭제 시
    fun DeleteList(listName:String){
        myRef.child(listName).removeValue()
    }

    //todolist 삭제 시
    fun DeleteTodo(listName: String, todoTitle: String){
        myRef.child(listName).child("cashbook-list").child(todoTitle).removeValue()
    }

    //산바다계곡
    fun sNum1(listName:String){
        AddTodo(listName,"식사비용", 100000, false)
        AddTodo(listName,"숙소비용", 200000, false)
        AddTodo(listName,"유류비", 100000, false)
    }
    fun sNum2(listName:String){
        AddTodo(listName,"1일차식사비용", 100000, false)
        AddTodo(listName,"2일차식사비용", 100000, false)
        AddTodo(listName,"숙소비용", 200000, false)
        AddTodo(listName,"유류비", 100000, false)
    }
    fun sNum3(listName:String){
        AddTodo(listName,"1일차식사비용", 100000, false)
        AddTodo(listName,"2일차식사비용", 100000, false)
        AddTodo(listName,"3일차식사비용", 100000, false)
        AddTodo(listName,"숙소비용", 200000, false)
        AddTodo(listName,"유류비", 100000, false)
    }
}