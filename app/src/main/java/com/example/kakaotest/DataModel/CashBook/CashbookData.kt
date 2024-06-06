package com.example.kakaotest.DataModel.CashBook

import java.io.Serializable

data class CashbookData(
    var itemName:String? = null,
    var itemCost: Int = 0,
    var isChecked:Boolean? = false,
) : Serializable