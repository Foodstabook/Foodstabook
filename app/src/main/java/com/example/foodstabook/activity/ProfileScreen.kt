package com.example.foodstabook.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.compiler.plugins.kotlin.EmptyFunctionMetrics.name
import androidx.compose.compiler.plugins.kotlin.lower.includeFileNameInExceptionTrace
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.foodstabook.R
import com.example.foodstabook.activity.ui.theme.FoodstabookTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase

private lateinit var db: FirebaseFirestore
private lateinit var reference: DatabaseReference
private lateinit var user_profile: FirebaseAuth
private var uid = user_profile.currentUser?.uid.toString()

class Profile : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reference = Firebase.database.reference

        setContent {
            FoodstabookTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
        //state: ProfileState
) {
        val mContext = LocalContext.current
        val notification = rememberSaveable { mutableStateOf("") }
        if (notification.value.isNotEmpty()) {
            Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
            notification.value = ""
        }
        user_profile = FirebaseAuth.getInstance()

    Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Cancel",
                        modifier = Modifier.clickable { notification.value = "Cancelled" })
                    Text(text = "Save",
                        modifier = Modifier.clickable { notification.value = "Profile updated" })
                }

                //Profile Pic
                ProfileImage()

                //Get User's data
//                reference = FirebaseDatabase.getInstance().getReference("Users")
//                if (uid.isNotEmpty()){
//                    getUserData(uid)
//                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    horizontalArrangement = Arrangement.Center

                ) {
                    user_profile.currentUser?.let {
                        it.displayName?.let { user_name ->
                            OutlinedTextField(
                                value = user_name,
                                onValueChange = { user_name },
                                placeholder = { Text("Enter Name")},
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color.Transparent,
                                    textColor = Color.Black
                                ),
                                label = { Text(text = "Name") }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 4.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        user_profile.currentUser?.let {
                            it.email?.let { user_email ->
                                OutlinedTextField(
                                    value = user_email,
                                    onValueChange = { user_email },
                                    placeholder = { Text("Enter Email")},
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = Color.Transparent,
                                        textColor = Color.Black
                                    ),
                                    label = { Text(text = "Email") }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        user_profile.signOut();
                        mContext.startActivity(Intent(mContext, SignUpActivity::class.java))
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEF6C00)),
                ) {
                    Text("Log Out", color = Color.Black)
                }
            }
        }


//fun getUserData(uid: String) {
//
//}


@Composable
    fun ProfileImage() {
        val imageUri = rememberSaveable { mutableStateOf("") }
        val painter = rememberImagePainter(
            if (imageUri.value.isEmpty())
                R.drawable.ic_user
            else
                imageUri.value
        )
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let { imageUri.value = it.toString() }
        }

        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable { launcher.launch("image/*") },
                    contentScale = ContentScale.Crop
                )
            }
            Text(text = "Change profile picture")
        }
    }

@Composable
fun promptUserLoginScreen(){
    val mContext = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.foodstabook_logo4),
            contentDescription = null
        )

        Text(
            "Sign In to view your Profile",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(150.dp)
        )

        Button(
            onClick = {
                mContext.startActivity(Intent(mContext, SignUpActivity::class.java))
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEF6C00)),
        ) {
            Text("Sign In", color = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    FoodstabookTheme() {
        user_profile = FirebaseAuth.getInstance()

        if (user_profile.currentUser != null) {
            ProfileScreen()
        }

        else {
            promptUserLoginScreen()
        }
    }
}