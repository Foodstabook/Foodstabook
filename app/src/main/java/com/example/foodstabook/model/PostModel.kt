package com.example.foodstabook.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Parcelize
data class PostModel(
    val postId: String,
    var postAuthor: String,
    var authorIcon: Int,
    var postTitle: String,
    val postImage: List<Int>,
    val postDate: LocalDateTime,
    var postDescription: String,
    var postTags: List<String>,
    var postRating: Int,
    var postLikes: Int,
    var postComments: List<CommentsModel>
) : Parcelable