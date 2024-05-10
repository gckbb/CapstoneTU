package com.example.kakaotest

import com.example.kakaotest.Map.RouteFeature
import com.google.gson.annotations.SerializedName

data class FeatureCollection(
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("features")
    val features: List<RouteFeature>? = null
)
