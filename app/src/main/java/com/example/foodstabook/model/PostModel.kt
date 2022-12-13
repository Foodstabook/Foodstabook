package com.example.foodstabook.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Date

@Parcelize
data class PostModel(
    val authorIcon: String = "",
    val postAuthor: String = "",
    val postComments: List<CommentsModel> = listOf(),
    val postDate: java.util.Date = Date(0),
    val postDescription: String = "",
    val postId: String = "",
    val postImage: List<String> = listOf(),
    val postLikes: Int = 0,
    val postRating: Int = 0,
    val postTags: List<String> = listOf(),
    val postTitle: String = ""
): Parcelable