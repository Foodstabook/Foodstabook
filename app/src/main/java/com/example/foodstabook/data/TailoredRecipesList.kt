package com.example.foodstabook.data

data class TailoredRecipesList(
    val results: List<Result>,
    val offset: Long,
    val number: Long,
    val totalResults: Long
)
