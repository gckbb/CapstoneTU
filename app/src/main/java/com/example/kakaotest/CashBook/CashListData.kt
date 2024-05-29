package com.example.kakaotest.CashBook

import java.io.Serializable

data class CashListData(
    var listName: String? = null,
    var todoTimestamp: String? = null,
) : Serializable //intent로 넘기기 위해 상속

