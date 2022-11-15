package com.example.foodstabook.activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.input.ImeAction
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
import kotlin.math.roundToInt


private lateinit var user: FirebaseAuth
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

    var rating: Float by remember { mutableStateOf(0f) }
    var ratingRound = (rating * 10.0).roundToInt() / 10.0

    val imageBackground = ImageBitmap.imageResource(id = R.drawable.star_background)
    val imageForeground = ImageBitmap.imageResource(id = R.drawable.star_foreground)


    val (focusTitle,focusHashtags,focusPlace,focusDescription) = remember { FocusRequester.createRefs() }
    val focusManager = LocalFocusManager.current

    val maxChar = 20;
    val maxCharD = 50;

    Scaffold(){
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
                    hashtags = it.take(maxChar)
                    if (it.length > maxChar){
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

            RatingBar(
                rating = rating,
                space = 2.dp,
                imageEmpty = imageBackground,
                imageFilled = imageForeground,
                animationEnabled = false,
                gestureEnabled = true,
                itemSize = 35.dp
            ) {
                rating = it
            }

            Text(
                "Rating: $ratingRound",
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

            Spacer(modifier = Modifier.height(150.dp))
            btnPost(title,hashtags,place,description,ratingRound,uid)
            Spacer(modifier = Modifier.height(8.dp))
            btnCancel()
            Spacer(modifier = Modifier.height(8.dp))
            btnShow()
        }
    }
}

@Composable
fun btnPost(
    title: String,
    hashtags: String,
    place: String,
    description: String,
    ratingRound: Double,
    uid: String
) {
    val mContext = LocalContext.current
    Button(
        onClick = {
            reference = FirebaseDatabase.getInstance().getReference("users")
            reference.child(uid).get().addOnSuccessListener {
                if(it.exists()){
                    val username = it.child("username").value.toString()
                    val posts: MutableMap<String, Any> = HashMap()
                    posts["Title"] = title
                    posts["Place"] = place
                    posts["Rating"] = ratingRound
                    posts["Description"] = description
                    val hashtagList = hashtags.split(",").toTypedArray()
                    posts["UID"] =  uid

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
                }else{
                    Log.d(TAG,"User don't exist")
                }
            }.addOnFailureListener{
                Log.d(TAG,"Fail")
            }
        },
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
fun btnShow() {
    val mContext = LocalContext.current
    Button(
        onClick = {
            var uid = user.currentUser?.uid.toString()
            Toast.makeText(mContext, "$uid", Toast.LENGTH_SHORT).show()
        },
        enabled = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEF6C00))
    ) {
        Text(text = "Show", color = Color.White, fontSize = 16.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun CreatePostPreview() {
    FoodstabookTheme {
        Post()
    }
}
