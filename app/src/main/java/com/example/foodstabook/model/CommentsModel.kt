package com.example.foodstabook.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommentsModel(
    val authorIcon: String = "",
    val commentAuthor: String = "",
    val commentBody: String = ""
): Parcelable