package com.example.foodstabook.model

import java.text.SimpleDateFormat

data class PostModel(
    val postId: String,
    val postAuthor: String,
    val postImage: List<Int>,
    val postDate: SimpleDateFormat,
    var postDescription: String,
    var postLikes: Int,
    var postComments: List<CommentsModel>
)