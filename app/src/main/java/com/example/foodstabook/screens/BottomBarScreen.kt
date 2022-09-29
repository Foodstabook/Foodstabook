package com.example.foodstabook.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object FoodSuggestion: BottomBarScreen(
        route = "foodSuggestion",
        title = "FoodSuggestion",
        icon = Icons.Default.Search
    )
    object Profile: BottomBarScreen(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )
    object Post: BottomBarScreen(
        route = "post",
        title = "Post",
        icon = Icons.Default.Add
    )
}

