package com.example.kakaotest.DataModel.CheckList

import java.io.Serializable

data class CheckListData(
    val listName: String = "",
    val todoTimestamp: String = "",
    val sharecode: String = "",
):Serializable
 //intent로 넘기기 위해 상속