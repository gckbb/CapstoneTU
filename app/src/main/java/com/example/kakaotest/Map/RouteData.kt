package com.example.kakaotest.Map

import com.google.gson.annotations.SerializedName

data class RouteData(val totalTime:Number? = null,
                     val totalDistance:Number? = null,
                     val totalFare:Number? = null,
                     val taxiFare:Number? = null)