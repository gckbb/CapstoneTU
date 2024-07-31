package com.example.kakaotest.DataModel.CheckList

import java.io.Serializable

data class TodoListData(
    var todoTitle:String? = null,
    var todoContent:String? = null,
    var isChecked:Boolean? = false
) : Serializable