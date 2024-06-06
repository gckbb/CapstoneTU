package com.example.kakaotest.Community

import java.io.Serializable

data class PostData(
    var postTitle: String? =null,
    var postContent: String? = null,
    var postPhoto: String? = null,
    var timestamp: String? = null,
    var uid: String? = null
) : Serializable
