package com.example.foodstabook.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
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
import com.example.foodstabook.R

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
    var name by rememberSaveable { mutableStateOf("default name") }
    var username by rememberSaveable { mutableStateOf("default username") }
    var user_email by rememberSaveable { mutableStateOf("default user's email") }
    var bio by rememberSaveable { mutableStateOf("default bio") }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(8.dp),
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            TextField(
                value = name,
                onValueChange = { name = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.Black
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Username", modifier = Modifier.width(100.dp))
            TextField(
                value = username,
                onValueChange = { username = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.Black
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Email", modifier = Modifier.width(100.dp))
            TextField(
                value = user_email,
                onValueChange = { user_email = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.Black
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Bio", modifier = Modifier
                    .width(100.dp)
                    .padding(top = 8.dp)
            )
            TextField(
                value = bio,
                onValueChange = { bio = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.Black
                ),
                singleLine = false,
                modifier = Modifier.height(150.dp)
            )
        }

        Button(
            onClick = {
                mContext.startActivity(Intent(mContext, SignUpActivity::class.java))
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEF6C00)),
        ) {
            Text("Log Out", color = Color.Black)
        }
//        Button(
//            onClick = {
//                mContext.startActivity(Intent(mContext, LoginPage::class.java))
//            },
//            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
//        ) {
//            Text("Login", color = Color.Black)
//        }
//        Button(
//            onClick = {
//                mContext.startActivity(Intent(mContext, ResetPassword::class.java))
//            },
//            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
//        ) {
//            Text("Reset Password", color = Color.Black)
//        }
//        Button(
//            onClick = {
//                mContext.startActivity(Intent(mContext, SettingActivity::class.java))
//            },
//            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
//        ) {
//            Text("Settings", color = Color.Black)
//        }
//        Button(
//            onClick = {
//                mContext.startActivity(Intent(mContext, UserAccount::class.java))
//            },
//            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
//        ) {
//            Text("User Account", color = Color.Black)
//        }



    }
}

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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
        ProfileScreen()
}