package com.example.foodstabook.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat

@Parcelize
data class PostModel(
    val postId: String,
    var postAuthor: String,
    var authorIcon: Int,
    val postImage: List<Int>,
    val postDate: SimpleDateFormat,
    var postDescription: String,
    var postLikes: Int,
    var postComments: List<CommentsModel>
) : Parcelable