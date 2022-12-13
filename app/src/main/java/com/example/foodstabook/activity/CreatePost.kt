package com.example.foodstabook.activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodstabook.R
import com.example.foodstabook.activity.ui.theme.FoodstabookTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.example.foodstabook.activity.ProfileScreen

lateinit var user: FirebaseAuth
private lateinit var db: FirebaseFirestore
private lateinit var reference: DatabaseReference


class CreatePost : ComponentActivity() {

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
                    Post()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Post() {
    db = FirebaseFirestore.getInstance()
    user = FirebaseAuth.getInstance()
    var title by remember { mutableStateOf("") }
    var hashtags by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val uid = user.currentUser!!.uid

    var rating1: Int by remember { mutableStateOf(0) }

    val (focusTitle,focusHashtags,focusPlace,focusDescription) = remember { FocusRequester.createRefs() }
    val focusManager = LocalFocusManager.current

    val maxChar = 20;
    val maxCharHashtag = 30;
    val maxCharD = 100;

    Scaffold(){
        val mContext = LocalContext.current
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
        ){
            OutlinedTextField(value = title, label = {Text(text = "Title")},
                onValueChange = {
                    title = it.take(maxChar)
                    if (it.length > maxChar){
                        focusManager.moveFocus(FocusDirection.Down) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusTitle),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)})
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = hashtags, label = {Text(text = "Hashtag")},
                onValueChange = {
                    hashtags = it.take(maxCharHashtag)
                    if (it.length > maxCharHashtag){
                        focusManager.moveFocus(FocusDirection.Down) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusHashtags),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onDone = {focusManager.moveFocus(FocusDirection.Down)})
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = place, label = {Text(text = "Place")},
                onValueChange = {
                    place = it.take(maxChar)
                    if (it.length > maxChar){
                        focusManager.moveFocus(FocusDirection.Down) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusPlace),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onDone = {focusManager.moveFocus(FocusDirection.Down)})
            )
            Spacer(modifier = Modifier.height(8.dp))


            var rating = RatingBar(ratingNum = rating1)
            Text(
                "Rating: $rating",
                fontSize = 16.sp,
                color = Color(0xFFEF6C00)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = description, label = {Text(text = "Description")},
                onValueChange = {
                    description = it.take(maxCharD)
                    if (it.length <= maxCharD){
                        description  = it } },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .focusRequester(focusDescription),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()})
            )

            Spacer(modifier = Modifier.height(100.dp))
            btnImage()
            Spacer(modifier = Modifier.height(8.dp))
            btnPost(title, hashtags, place, description, rating, uid)
            Spacer(modifier = Modifier.height(8.dp))
            btnCancel()

        }
    }
}

@Composable
fun btnPost(
    title: String,
    hashtags: String,
    place: String,
    description: String,
    rating: Int,
    uid: String
) {
    val mContext = LocalContext.current
    Button(
        onClick = {
            if (inputValidation(title, hashtags, place, description, rating,mContext)) {
                reference = FirebaseDatabase.getInstance().getReference("users")
                reference.child(uid).get().addOnSuccessListener {
                    if (it.exists()) {
                        val username = it.child("username").value.toString()
                        val posts: MutableMap<String, Any> = HashMap()
                        posts["Title"] = title
                        posts["Place"] = place
                        posts["Rating"] = rating
                        posts["Description"] = description
                        val hashtagList = hashtags.split(",").toTypedArray()
                        posts["UID"] = uid

                        for ((index, h) in hashtagList.withIndex()) {
                            posts["Hashtag$index"] = h
                        }
                        posts["Username"] = username

                        db.collection("Test").add(posts).addOnSuccessListener {
                            Toast.makeText(mContext, "Post Success", Toast.LENGTH_SHORT).show()
                            mContext.startActivity(Intent(mContext, MainActivity::class.java))
                        }.addOnFailureListener {
                            Toast.makeText(mContext, "Post Failure.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.d(TAG, "User don't exist")
                    }
                }.addOnFailureListener {
                    Log.d(TAG, "Fail")
                }
            } },
        enabled = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEF6C00))
    ) {
        Text(text = "Post", color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun btnCancel() {
    val mContext = LocalContext.current
    Button(
        onClick = {
            mContext.startActivity(Intent(mContext, MainActivity::class.java))
        },
        enabled = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEF6C00))
    ) {
        Text(text = "Cancel", color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun btnImage() {
    val mContext = LocalContext.current
    Button(
        onClick = {

        },
        enabled = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEF6C00))
    ) {
        Text(text = "Image", color = Color.White, fontSize = 16.sp)
    }
}

fun inputValidation(
    title: String,
    hashtags: String,
    place: String,
    description: String,
    rating: Int,
    mContext: Context
): Boolean  {
    if (title == null || "".equals(title)) {
        Toast.makeText(mContext, "Please enter Title", Toast.LENGTH_LONG).show()
        return false;
    } else if(title.length>20){
        Toast.makeText(mContext, "Title no more than 20 characters.", Toast.LENGTH_LONG).show()
        return false;
    }

    if (hashtags == null || "".equals(hashtags)) {
        Toast.makeText(mContext, "Please enter Hashtag", Toast.LENGTH_LONG).show()
        return false;
    }else if(hashtags.length>30){
        Toast.makeText(mContext, "Hashtag no more than 20 characters.", Toast.LENGTH_LONG).show()
        return false;
    }

    if (place == null || "".equals(place)) {
        Toast.makeText(mContext, "Please enter Place", Toast.LENGTH_LONG).show()
        return false;
    }else if(place.length>20){
        Toast.makeText(mContext, "Place no more than 20 characters.", Toast.LENGTH_LONG).show()
        return false;
    }

    if (rating <= 0) {
        Toast.makeText(mContext, "Please Rate", Toast.LENGTH_LONG).show()
        return false;
    }

    if (description == null || "".equals(description) ) {
        Toast.makeText(mContext, "Please enter Description", Toast.LENGTH_LONG).show()
        return false;
    }else if(description.length>100){
        Toast.makeText(mContext, "Description no more than 100 characters.", Toast.LENGTH_LONG).show()
        return false;
    }
    return true
}

@Preview(showBackground = true)
@Composable
fun CreatePostPreview() {
    FoodstabookTheme {
        user = FirebaseAuth.getInstance()

        if(user.currentUser != null) {
            Post()
        }
        else{
            promptUserLoginScreen()
        }
    }
}

