package com.example.foodstabook.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodstabook.*
import com.example.foodstabook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContent {
            Scaffold()
        }

        //setContentView(view)

        //To force change the title of the activity
        title = "Main Menu"

        binding.profileButton.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        goToResetPassword()
        goToFoodSuggestion()
        goToUserAccount()
        goToSignIn()
        goToSignUp()
        goToSettings()
        goToNewsfeed()
        goHome()
        //NewsfeedScreen()
    }

    private fun goToResetPassword(){
        binding.resetPasswordButton.setOnClickListener{
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }
    }

    private fun goToFoodSuggestion(){
        binding.foodSuggestionButton.setOnClickListener{
            val intent = Intent(this, SuggestionMainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToUserAccount(){
        binding.userAccountButton.setOnClickListener{
            val intent = Intent(this, UserAccount::class.java)
            startActivity(intent)
        }
    }

    private fun goHome(){
        binding.logo.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToSignIn(){
        binding.signInButton.setOnClickListener{
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }

    private fun goToSignUp(){
        binding.signUpButton.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToSettings(){
        binding.settingsButton.setOnClickListener{
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToNewsfeed(){
        binding.newsfeedButton.setOnClickListener{
            val intent = Intent(this, NewsfeedActivity::class.java)
            startActivity(intent)
        }
    }
}

//Create Scaffold Composable functions
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Scaffold() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        BottomNavGraph(navController = navController)
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

    BottomNavigation {
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
@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "profile"
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
    Column (modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Home",
             fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun Profile(navController: NavHostController){
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Profile",
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun Post(navController: NavHostController){
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Post",
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun FoodSuggestion(navController: NavHostController){
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Food Suggestion",
            fontWeight = FontWeight.ExtraBold
        )
    }
}
