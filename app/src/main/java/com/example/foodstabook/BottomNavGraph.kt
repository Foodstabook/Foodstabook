package com.example.foodstabook

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.example.foodstabook.activity.ProfileActivity
import com.example.foodstabook.activity.SuggestionMainActivity
import com.example.foodstabook.screens.BottomBarScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route){
            NewsfeedScreen()
        }
        composable(route = BottomBarScreen.Profile.route){
            ProfileActivity()
        }
        composable(route = BottomBarScreen.FoodSuggestion.route){
            SuggestionMainActivity()
        }
        composable(route = BottomBarScreen.Post.route){
            //PostScreen()
        }
    }
}