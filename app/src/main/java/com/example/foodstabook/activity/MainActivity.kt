package com.example.foodstabook.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
        object Home : BottomBarItem(
            route = "home",
            title = "Home",
            icon = Icons.Default.Home
        )

        object FoodSuggestion : BottomBarItem(
            route = "foodSuggestion",
            title = "Suggestion",
            icon = Icons.Default.Search
        )

        object Profile : BottomBarItem(
            route = "profile",
            title = "Profile",
            icon = Icons.Default.Person
        )

        object Post : BottomBarItem(
            route = "post",
            title = "Post",
            icon = Icons.Default.Add
        )
    }

//Create BottomNavigationBar Composable function in which
// you click each object then it switch to the selected activity

    @Composable
    fun BottomBar(navController: NavHostController) {
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
                AddItem(
                    screen = bottomBarItem,
                    currentDestination = currentDestination,
                    navController = navController
                )
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
                Home(navController = navController)
            }
            composable(route = "profile") {
                Profile(navController = navController)
            }
            composable(route = BottomBarItem.FoodSuggestion.route) {
                FoodSuggestion(navController = navController)
            }
            composable(route = BottomBarItem.Post.route) {
                Post(navController = navController)
            }
        }

    }

    @Composable
    fun Home(navController: NavHostController) {
        val mContext = LocalContext.current
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Home",
                fontWeight = FontWeight.ExtraBold
            )
            Button(
                onClick = {
                    mContext.startActivity(Intent(mContext, NewsfeedActivity::class.java))
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            ) {
                Text("Newsfeed", color = Color.Black)
            }
        }
    }

<<<<<<< HEAD
    @Composable
    fun Profile(navController: NavHostController) {
        val mContext = LocalContext.current
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
=======
}

@Composable
fun Home(navController: NavHostController){
    /*AndroidView(
        factory = {
            View.inflate(it, R.layout.activity_newsfeed, null)
        },
        modifier = Modifier.fillMaxSize()
    )*/

    val mContext = LocalContext.current
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Home",
            fontWeight = FontWeight.ExtraBold
        )
        Button(
            onClick = {
                mContext.startActivity(Intent(mContext, NewsfeedActivity::class.java))
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
>>>>>>> Retrospective2_Tram2
        ) {
            Text(
                text = "Profile",
                fontWeight = FontWeight.ExtraBold
            )
            Button(
                onClick = {
                    mContext.startActivity(Intent(mContext, ProfileActivity::class.java))
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            ) {
                Text("Profile", color = Color.Black)
            }
            Button(
                onClick = {
                    mContext.startActivity(Intent(mContext, SignUpActivity::class.java))
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            ) {
                Text("Sign Up", color = Color.Black)
            }
            Button(
                onClick = {
                    mContext.startActivity(Intent(mContext, LoginPage::class.java))
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            ) {
                Text("Login", color = Color.Black)
            }
            Button(
                onClick = {
                    mContext.startActivity(Intent(mContext, ResetPassword::class.java))
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            ) {
                Text("Reset Password", color = Color.Black)
            }
            Button(
                onClick = {
                    mContext.startActivity(Intent(mContext, SettingActivity::class.java))
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            ) {
                Text("Settings", color = Color.Black)
            }
            Button(
                onClick = {
                    mContext.startActivity(Intent(mContext, UserAccount::class.java))
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            ) {
                Text("User Account", color = Color.Black)
            }
            Button(
                onClick = {
                    mContext.startActivity(Intent(mContext, CreatePost::class.java))
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            ) {
                Text("Create Post", color = Color.Black)
            }
        }
    }

<<<<<<< HEAD
    @Composable
    fun Post(navController: NavHostController) {
        val mContext = LocalContext.current
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
=======
@Composable
fun Profile(navController: NavHostController){
    /*AndroidView(
        factory = {
            View.inflate(it, R.layout.activity_profile, null)
        },
        modifier = Modifier.fillMaxSize()
    )*/

    val mContext = LocalContext.current
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Profile",
            fontWeight = FontWeight.ExtraBold
        )
        Button(
            onClick = {
                mContext.startActivity(Intent(mContext, ProfileActivity::class.java))
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
>>>>>>> Retrospective2_Tram2
        ) {
            Text(
                text = "Post",
                fontWeight = FontWeight.ExtraBold
            )
        }
    }

<<<<<<< HEAD
    @Composable
    fun FoodSuggestion(navController: NavHostController) {
        val mContext = LocalContext.current
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
=======
@Composable
fun Post(navController: NavHostController){
    AndroidView(
        factory = {
            View.inflate(it, R.layout.activity_create_post, null)
        },
        modifier = Modifier.fillMaxSize()
    )

    /*Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Post",
            fontWeight = FontWeight.ExtraBold
        )
    }*/
}

@Composable
fun FoodSuggestion(navController: NavHostController){
    /*AndroidView(
        factory = {
            View.inflate(it, R.layout.activity_suggestion_main, null)
        },
        modifier = Modifier.fillMaxSize()
    )*/

    val mContext = LocalContext.current
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Food Suggestion",
            fontWeight = FontWeight.ExtraBold
        )
        Button(
            onClick = {
                mContext.startActivity(Intent(mContext, SuggestionMainActivity::class.java))
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
>>>>>>> Retrospective2_Tram2
        ) {
            Text(
                text = "Food Suggestion",
                fontWeight = FontWeight.ExtraBold
            )
            Button(
                onClick = {
                    mContext.startActivity(Intent(mContext, SuggestionMainActivity::class.java))
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            ) {
                Text("Food Suggestion", color = Color.Black)
            }
        }
    }
}