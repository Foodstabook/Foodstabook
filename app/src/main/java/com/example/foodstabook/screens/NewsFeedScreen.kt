package com.example.foodstabook

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewsfeedScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {}
    ) {
        BottomNavGraph(navController = navController)
    }

@Composable
fun BottomBar(navController: NavHostController){
    val
}
}