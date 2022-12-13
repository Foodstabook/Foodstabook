package com.example.foodstabook.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.foodstabook.R
import com.example.foodstabook.model.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_newsfeed.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

private lateinit var storage2: FirebaseStorage
private val screenState: MutableState<Int> = mutableStateOf(0)
private val selectedIndex = mutableStateOf(0)
private var postSearchResults = mutableStateListOf<PostModel>()
private var userSearchResults = mutableStateListOf<UserModel>()
private var postList = getPostListFromBackend()
private var userList = ArrayList<UserModel>()
private var searchTextState = mutableStateOf("")
private var searchedText = mutableStateOf("")

fun generateUsersList(){
    userList.add(UserModel("Testing1", "Steven", "Barker", 31, "Sbarker@mail.com", R.drawable.foodstabook_logo4))
    userList.add(UserModel("Testing2", "Maria", "Love", 25, "LoveMaria@mail.com", R.drawable.foodstabook_logo4))
    userList.add(UserModel("Testing3", "Janine", "Lane", 39, "JLane@mail.com", R.drawable.foodstabook_logo4))
    userList.add(UserModel("Testing4", "Tyler", "Oner", 23, "TOner@mail.com", R.drawable.foodstabook_logo4))
}

//Composable function for each item returned in newsfeed views
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PostCard(post: PostModel) {
    val numLikes = remember { mutableStateOf(post.postLikes) }
    val liked = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val imagePosition = remember { mutableStateOf(0) }
    val expanded = remember { mutableStateOf(false) }
    val backArrowVisible = remember { mutableStateOf(false)}
    val forwardArrowVisible = remember { mutableStateOf(true)}
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = MutableInteractionSource()
    val tags = addHashtags(post)
    Card(
        modifier = Modifier
            .fillMaxSize(1f),
        elevation = 0.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Image(painter = rememberAsyncImagePainter(model = post.postImage[0]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(60.dp)
                        .offset(x = 4.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                )
                Text(post.postAuthor,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .offset(x = 6.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        })
            }
            Box {
                Image(painter = rememberAsyncImagePainter(model = post.postImage[imagePosition.value]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            },
                                onDoubleTap = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    if (!liked.value) {
                                        numLikes.value += 1
                                        liked.value = true
                                    } else {
                                        numLikes.value -= 1
                                        liked.value = false
                                    }
                                })
                        }
                )
                if (imagePosition.value != 0) {
                    if (backArrowVisible.value) {
                        Image(painter = painterResource(id = R.drawable.back_arrow),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .size(36.dp)
                                .alpha(0.7f)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    if (imagePosition.value != 0) {
                                        imagePosition.value -= 1
                                        forwardArrowVisible.value = true
                                    }
                                    backArrowVisible.value =
                                        imagePosition.value != post.postImage.size - 1
                                }
                        )
                    }
                }
                if(imagePosition.value != post.postImage.size - 1){
                    if (forwardArrowVisible.value) {
                        Image(painter = painterResource(id = R.drawable.forward_arrow),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(36.dp)
                                .alpha(0.7f)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    if (imagePosition.value != post.postImage.size - 1) {
                                        imagePosition.value += 1
                                        backArrowVisible.value = true
                                    }
                                    forwardArrowVisible.value =
                                        imagePosition.value != post.postImage.size - 1
                                }
                        )
                    }
                }
            }
            Text(post.postTitle,
                fontFamily = FontFamily.Default,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .offset(x = 4.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
            )
            Text("Likes: " + numLikes.value.toString(),
                color = Color.Gray,
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .offset(x = 4.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    })

            Column(modifier = Modifier
                .animateContentSize(animationSpec = tween(100))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    expanded.value = !expanded.value
                }) {

                if (expanded.value) {
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = Color.Black)){
                                append(post.postDescription + " ")
                            }
                            withStyle(SpanStyle(color = Color.Cyan)){
                                append(tags)
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default)
                } else {
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = Color.Black)) {
                                append(post.postDescription + " ")
                            }
                            withStyle(SpanStyle(color = Color.Cyan)) {
                                append(tags)
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default)
                }
            }

            Text("View all ${post.postComments.size} comments",
                fontSize = 16.sp,
                fontFamily = FontFamily.Default,
                color = Color.Gray,
                modifier = Modifier
                    .clickable {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        val intent = Intent(context, CommentsActivity::class.java)
                        intent.putExtra("clickedPost", post)
                        context.startActivity(intent)
                    }
                    .offset(4.dp))
        }
    }
}

//Function for adding # before tags
private fun addHashtags(post: PostModel): String {
    var tags = ""

    for (i in post.postTags) {
        val tag = "#" + i.filter { !it.isWhitespace() } + " "
        tags += tag.lowercase()
    }
    return tags
}

//Function for searching for posts based on a user entered keyword
private fun searchPosts(searchText: String, posts: PostsList): PostsList {
    val returnedPosts = PostsList()
    val regex = Regex("\\b(${searchText})\\b")
    for (post in posts) {
        var postAdded = false
        for (tag in post.postTags) {
            if (tag.equals(searchText, ignoreCase = true)) {
                returnedPosts.add(post)
                postAdded = true
            }
        }
        if (!postAdded) {
            if (regex.containsMatchIn(post.postDescription))
                returnedPosts.add(post)
        }
    }
    return returnedPosts
}

private fun searchUsers(searchText: String, users: List<UserModel>): MutableList<UserModel> {
    val returnedUsers = mutableListOf<UserModel>()
    for (user in users){
        if(user.userName.equals(searchText, ignoreCase = true))
            returnedUsers.add(user)
    }
    return returnedUsers
}

//Function for displaying posts on the newsfeed
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LazyNewsfeed(posts: PostsList) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val state by screenState
    val textState by searchTextState
    val searchedKeyword by searchedText
    val searchRefresh = remember {mutableStateOf(false)}
    val coroutineScope = rememberCoroutineScope()
    val searchState = rememberLazyListState()
    val selectedPost by selectedIndex
    Column {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if(state == 1) {
                Text(
                    text = "<",
                    modifier = Modifier
                        .clickable {
                            screenState.value = 0
                            searchTextState.value = ""
                        }
                        .offset(8.dp),
                    fontSize = 40.sp
                )
            }
            else if(state == 2) {
                Text(
                    text = "<",
                    modifier = Modifier
                        .clickable {
                            screenState.value = 1
                        }
                        .offset(8.dp),
                    fontSize = 40.sp,
                    textAlign = TextAlign.Start
                )
            }
            if(state == 0 || state == 1) {
                TextField(
                    value = textState,
                    onValueChange = {
                        searchTextState.value = it
                    },
                    placeholder = { Text(text = "Search") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon"
                        )
                    },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear Icon",
                            modifier = Modifier.clickable { searchTextState.value = ("")
                                focusManager.clearFocus()
                                keyboardController?.hide()})
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
                        searchedText.value = textState
                        searchRefresh.value = true
                        screenState.value = 1
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
            else{
                Spacer(modifier = Modifier.weight(1f))
                Text(text = textState,
                    textAlign = TextAlign.Center,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(x = (-10).dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        when (state) {
            0 -> {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth(1f)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        })
                    },
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(posts) { post ->
                        PostCard(post)
                    }
                }
            }
            1 -> {
                if (searchRefresh.value) {
                    userSearchResults.clear()
                    postSearchResults.clear()
                    userSearchResults.addAll(searchUsers(searchedKeyword, userList))
                    postSearchResults.addAll(searchPosts(searchedKeyword, postList))
                    searchRefresh.value = false
                }
                LazyColumn(state = searchState,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            })
                        },
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(userSearchResults){ item ->
                        Row(modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .pointerInput(Unit) { detectTapGestures() },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start){
                            Image(painter = painterResource(id = item.userIcon),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(125.dp)
                                    .aspectRatio(1f)
                                    .clip(CircleShape))
                            Column(
                                Modifier
                                    .padding(start = 8.dp)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(text = item.userName,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp)
                                Text(text = item.firstName + " " + item.lastName,
                                    fontSize = 16.sp)
                            }
                        }
                    }
                    itemsIndexed(postSearchResults ) { index: Int, item: PostModel ->
                        /*val timeSincePost = if (ChronoUnit.SECONDS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()) < 60)
                            "1 second ago"

                        else if (ChronoUnit.SECONDS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()) < 60)
                            ChronoUnit.SECONDS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()).toString() + " seconds ago"

                        else if (ChronoUnit.MINUTES.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()) < 60)
                            ChronoUnit.MINUTES.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()).toString() + " minutes ago"

                        else if (ChronoUnit.HOURS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()) == 1L)
                            "1 hour ago"

                        else if (ChronoUnit.HOURS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()) < 24)
                            ChronoUnit.HOURS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()).toString() + " hours ago"

                        else if (ChronoUnit.DAYS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()) == 1L)
                            "1 day ago"

                        else if (ChronoUnit.DAYS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()) < 14)
                            ChronoUnit.DAYS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()).toString() + " days ago"

                        else if (ChronoUnit.WEEKS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()) < 5)
                            ChronoUnit.WEEKS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()).toString() + " weeks ago"

                        else if (ChronoUnit.MONTHS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()) == 1L)
                            "1 month ago"

                        else if (ChronoUnit.MONTHS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()) < 12)
                            ChronoUnit.MONTHS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()).toString() + " months ago"

                        else if (ChronoUnit.YEARS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()) == 1L)
                            "1 year ago"

                        else
                            ChronoUnit.YEARS.between(LocalDateTime.parse(item.postDate.toString()), LocalDateTime.now()).toString() + " years ago"

                         */
                        Row(modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .pointerInput(Unit) { detectTapGestures(onTap = { screenState.value = 2
                                selectedIndex.value = index}) },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start){
                            Image(painter = rememberAsyncImagePainter(model = item.postImage[0]),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(125.dp)
                                    .aspectRatio(1f))
                            Column(
                                Modifier
                                    .padding(start = 8.dp)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(text = item.postTitle,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp)
                                Text(text = item.postLikes.toString() + " likes",
                                    fontSize = 16.sp)
                                Text(text = item.postComments.size.toString() + " comments",
                                    fontSize = 16.sp)
                                /*Text(text = timeSincePost,
                                    fontSize = 14.sp,
                                    color = Color.Gray)*/
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier
                    .weight(1f)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        })
                    })
            }
            2 -> {
                LazyColumn(state = searchState,
                    modifier = Modifier.fillMaxWidth(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    coroutineScope.launch {
                        searchState.animateScrollToItem(index = selectedPost)
                    }
                    items(postSearchResults) { result ->
                        PostCard(result)
                    }
                }
            }
        }
    }
}

fun getPostListFromBackend(): PostsList {
    val postsToReturn = PostsList()
    val storage = Firebase.storage
    val storageRef = storage.reference
    var imagesRef = storageRef.child("ImagesFolder")
    var userIconsRef = storageRef.child("UserIcons")
    val firestore = Firebase.firestore
    firestore.collection("Post")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                var postItem: PostModel
                var authorIconUrl: MutableState<String> = mutableStateOf("")
                val imageUrlList: MutableList<String> = mutableListOf()
                val ref = firestore.collection("Post").document(document.id)
                ref.get().addOnSuccessListener{documentSnapshot ->
                    postItem = documentSnapshot.toObject<PostModel>()!!
                    storage.getReferenceFromUrl(postItem.authorIcon).downloadUrl.addOnSuccessListener(){
                        authorIconUrl.value = it.toString()
                        Log.d(TAG, "Author Icon: $it")
                    }.addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting author Icon: ", exception)
                    }
                    for(image in postItem.postImage){
                         storage.getReferenceFromUrl(image).downloadUrl.addOnSuccessListener(){
                            imageUrlList.add(it.toString())
                            Log.d(TAG, "Image: $it")
                        }.addOnFailureListener { exception ->
                            Log.d(TAG, "Error getting post images: ", exception)
                        }
                    }
                    Log.d(TAG, "Date Time: ${postItem.postDate}")
                    Log.d(TAG, "Author Icon: ${authorIconUrl.value}")
                    postsToReturn.add(PostModel(authorIconUrl.value, postItem.postAuthor, postItem.postComments, postItem.postDate, postItem.postDescription,
                        postItem.postId, imageUrlList, postItem.postLikes, postItem.postRating, postItem.postTags, postItem.postTitle))
                }.addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting post: ", exception)
                }

            }
        }
        .addOnFailureListener { exception ->
            Log.d(TAG, "Error getting documents: ", exception)
        }
    return postsToReturn
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun NewsfeedPreview(){
    if(postList.isEmpty())
        Log.d(TAG, "List was empty")
        postList.addAll(getPostListFromBackend())
    if(userList.isEmpty())
        generateUsersList()
    MaterialTheme{
        LazyNewsfeed(posts = postList)
    }
}

