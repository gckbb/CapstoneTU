package com.example.kakaotest.CashBook

import java.io.Serializable

data class CashbookData(
    var itemName:String? = null,
    var itemCost: Int = 0,
    var isChecked:Boolean? = false,
) : Serializable
