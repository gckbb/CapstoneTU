package com.example.kakaotest.Plan

import com.google.gson.annotations.SerializedName

data class PFeatureCollection(
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("features")
    val features: List<PRouteFeature>? = null
)
