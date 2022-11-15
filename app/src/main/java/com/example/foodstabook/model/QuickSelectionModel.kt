package com.example.foodstabook.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class QuickSelectionModel(val title: String, val image: Int,
                               val isSelected: MutableState<Boolean> = mutableStateOf(false),
                               val isType: Boolean,
                               val isCuisine: Boolean)
