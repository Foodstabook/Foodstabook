package com.example.foodstabook.activity

import ProfileState
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProfileScreen(
    //state: ProfileState
) {
    val mContext = LocalContext.current
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
            ){
        Text(text = "Profile")
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
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ProfilePreview() {
//    MaterialTheme {
//        ProfileScreen()
//    }
//}