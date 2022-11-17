package com.example.foodstabook.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class DietsSelectionModel(val title: String, val ORisSelected: MutableState<Boolean> = mutableStateOf(false),
                                val ANDisSelected: MutableState<Boolean> = mutableStateOf(false))
