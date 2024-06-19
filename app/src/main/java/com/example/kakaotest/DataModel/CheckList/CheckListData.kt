package com.example.kakaotest.DataModel.CheckList

import java.io.Serializable

data class CheckListData(
    val listName: String = "",
    val todoTimestamp: String = "",
    val sharecode: String = "",
    val userIds: MutableList<String> = mutableListOf()
):Serializable