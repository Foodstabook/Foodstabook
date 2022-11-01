package com.example.foodstabook.activity.ui.theme

import android.content.Intent
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.foodstabook.activity.CommentsActivity
import com.example.foodstabook.activity.ProfileActivity

class PostCard {
}

@Composable
fun PostCard(userIcon: Int, authorIcon: Int, postAuthor: String, postImage: List<Int>, likes: Int, postDescription: String,
numComments: Int){
    val numLikes = remember { mutableStateOf(likes)}
    val liked = remember{ mutableStateOf(false)}
    val context = LocalContext.current
    val imagePosition = remember{ mutableStateOf(0)}
    val expanded = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
            elevation = 5.dp){
        Column() {
            Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()){
                AndroidView(factory = { xmlContext ->
                    android.widget.SearchView(xmlContext).apply {
                        queryHint = "Search"
                    }},
                modifier = Modifier.padding(end = 50.dp))
                Image(painter = painterResource(id = userIcon),
                contentDescription = null,
                    alignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(48.dp)
                        .clickable { val intent = Intent(context, ProfileActivity::class.java)
                        context.startActivity(intent)}
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End){
                Image(painter = painterResource(id = authorIcon),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(60.dp)
                        .clickable {  }
                )
                Text(postAuthor)
            }
            Box(){
                Image(painter = painterResource(id = postImage[imagePosition.value]),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .pointerInput(Unit) { detectTapGestures(onDoubleTap = { if(!liked.value){
                        numLikes.value += 1
                        liked.value = true
                    }
                    else{
                        numLikes.value -= 1
                        liked.value = false
                    }})
                    }
                )
                Image(painter = painterResource(id = com.example.foodstabook.R.drawable.back_arrow),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(36.dp)
                        .alpha(0.8f)
                        .clickable { imagePosition.value -= 1 })
                Image(painter = painterResource(id = com.example.foodstabook.R.drawable.forward_arrow),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(36.dp)
                        .alpha(0.8f)
                        .clickable { imagePosition.value += 1 })
            }
            Text("Likes: " + numLikes.value.toString())

            Column(modifier = Modifier
                .animateContentSize(animationSpec = tween(100))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { expanded.value = !expanded.value }) {

                if (expanded.value) {
                    Text(text = postDescription)
                } else {
                    Text(text = postDescription, maxLines = 3, overflow = TextOverflow.Ellipsis)
                }
            }

            Text("View all $numComments comments",
            modifier = Modifier.clickable {
                val intent = Intent(context, CommentsActivity::class.java)
            })
        }
    }
}

@Composable
@Preview(showBackground = true)
fun postPreview() {
    val context = LocalContext.current
    MaterialTheme {
        PostCard(
            com.example.foodstabook.R.drawable.default_profile_photo,
            com.example.foodstabook.R.drawable.default_profile_photo,
            "TestUser",
            listOf(com.example.foodstabook.R.drawable.blueberry_crumble,
            com.example.foodstabook.R.drawable.borscht),
            100, context.getString(com.example.foodstabook.R.string.post_description_1), 3
        )
    }
}