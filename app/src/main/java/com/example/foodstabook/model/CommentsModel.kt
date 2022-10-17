package com.example.foodstabook.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommentsModel(
    val authorIcon: Int,
    val commentAuthor: String,
    var commentBody: String
) : Parcelable
