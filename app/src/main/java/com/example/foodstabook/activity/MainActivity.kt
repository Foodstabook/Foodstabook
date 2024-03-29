package com.example.foodstabook.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import com.example.foodstabook.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodstabook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContent {
            Scaffold()
        }

        //setContentView(view)

        //To force change the title of the activity
        title = "Main Menu"
        }
    }

//Create Scaffold Composable functions
@Preview
@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Scaffold() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        Box(modifier = Modifier.padding(it)) {
            BottomNavGraph(navController = navController)
        }
    }
}

//Create Destination
sealed class BottomBarItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: BottomBarItem(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object FoodSuggestion: BottomBarItem(
        route = "foodSuggestion",
        title = "Suggestion",
        icon = Icons.Default.Search
    )
    object Profile: BottomBarItem(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )
    object Post: BottomBarItem(
        route = "post",
        title = "Post",
        icon = Icons.Default.Add
    )
}

//Create BottomNavigationBar Composable function in which
// you click each object then it switch to the selected activity
@Composable
fun BottomBar(navController: NavHostController){
    val screens = listOf(
        BottomBarItem.FoodSuggestion,
        BottomBarItem.Home,
        BottomBarItem.Post,
        BottomBarItem.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(backgroundColor = Color.White) {
        screens.forEach { bottomBarItem ->
            AddItem(screen = bottomBarItem, currentDestination = currentDestination, navController = navController)
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarItem,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route)
        }
    )
}

//Set up Navigation Route
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "profile",
    ) {
        composable(route = "home") {
            Home(navController= navController)
        }
        composable(route = "profile") {
            Profile(navController= navController)
        }
        composable(route = BottomBarItem.FoodSuggestion.route) {
            FoodSuggestion(navController= navController)
        }
        composable(route = BottomBarItem.Post.route) {
            Post(navController= navController)
        }
    }

}
@Composable
fun Home(navController: NavHostController){
    ConstraintLayout {
        NewsfeedPreview()
    }
}

@Composable
fun Profile(navController: NavHostController){
    ConstraintLayout {
        ProfileScreen()
    }
}

@Composable
fun Post(navController: NavHostController){
    ConstraintLayout {
        CreatePostPreview()
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun FoodSuggestion(navController: NavHostController){
        SuggestionCreator()
}