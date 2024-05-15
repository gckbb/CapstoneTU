package com.example.kakaotest.DataModel

data class PostData (
    var postCount: Int? = null, // 게시글 개수
    var postList : List<PostList>? = null,
    var likePost : List<String>? = null
)
data class PostList(
    var postId: String? = null,
    var title: String? = null,
    var content: String? = null,

)