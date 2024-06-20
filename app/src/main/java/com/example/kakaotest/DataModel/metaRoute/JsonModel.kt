package com.example.kakaotest.DataModel.metaRoute

import kotlinx.parcelize.Parcelize

@Parcelize
data class JsonModel(
    val startX:String,
    val startY:String,
    val endX:String,
    val endY:String,
    val count:Int,
    val lang:Int,
    val format:String
)