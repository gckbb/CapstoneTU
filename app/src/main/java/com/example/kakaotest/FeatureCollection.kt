package com.example.kakaotest

import com.google.gson.annotations.SerializedName

data class FeatureCollection(
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("features")
    val features: List<RouteFeature>? = null
)