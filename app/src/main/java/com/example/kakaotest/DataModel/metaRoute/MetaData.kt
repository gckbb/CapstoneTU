package com.example.kakaotest.DataModel.metaRoute

import com.google.gson.annotations.SerializedName

//complete
data class MetaData(
    @SerializedName("requestParameters")
    val requestParameters:RequestParameters,
    @SerializedName("plan")
    val plan:Plan
    )
