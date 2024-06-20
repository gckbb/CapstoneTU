package com.example.kakaotest.DataModel.CashBook

import java.io.Serializable

data class CashListData(
    var listName: String? = null,
    var todoTimestamp: String? = null
) : Serializable
