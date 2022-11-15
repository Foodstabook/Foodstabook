package com.example.foodstabook.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class PreferencesSelectionModel(val title: String,
                                     val isSelected: MutableState<Boolean> = mutableStateOf(false))
