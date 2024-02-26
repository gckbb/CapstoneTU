package com.example.kakaotest.Post

data class PostData (
    var postId: String? = null,
    var title: String? = null,
    var content: String? = null,
    // 기타 게시글 관련 필드 추가
)