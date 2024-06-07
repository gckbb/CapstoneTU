package com.example.kakaotest.DataModel.metaRoute

//complete
data class RequestParameters(
    val busCount: Int,
    val subwayBusCount: Int,
    val expressbusCount: Int,
    val trainCount: Int,
    val airplaneCount: Int,
    val ferryCount: Int,
    val wideareaRouteCount: Int,
    val startX: String,
    val startY: String,
    val endX: String,
    val endY: String,
    val locale: String,
    val reqDttm: String
)
