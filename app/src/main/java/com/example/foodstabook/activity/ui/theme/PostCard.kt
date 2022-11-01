package com.example.foodstabook.activity.ui.theme

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import com.example.foodstabook.activity.recipeBuilder
import com.example.foodstabook.model.CommentsModel
import com.example.foodstabook.model.PostModel
import java.text.SimpleDateFormat

class PostCard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PostCard()
        }
    }
}

@Composable
fun PostCard(post: PostModel) {
    val numLikes = remember { mutableStateOf(post.postLikes) }
    val liked = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val imagePosition = remember { mutableStateOf(0) }
    val expanded = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = 0.dp
    ) {
        Column() {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Image(painter = painterResource(id = post.authorIcon),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(60.dp)
                        .clickable { }
                )
                Text(post.postAuthor)
            }
            Box() {
                Image(painter = painterResource(id = post.postImage[imagePosition.value]),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(onDoubleTap = {
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
                Image(painter = painterResource(id = com.example.foodstabook.R.drawable.back_arrow),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(36.dp)
                        .alpha(0.8f)
                        .clickable {
                            if (imagePosition.value != 0) {
                                imagePosition.value -= 1
                            }
                        })
                Image(painter = painterResource(id = com.example.foodstabook.R.drawable.forward_arrow),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(36.dp)
                        .alpha(0.8f)
                        .clickable {
                            if (imagePosition.value != post.postImage.size - 1) {
                                imagePosition.value += 1
                            }
                        })
            }
            Text("Likes: " + numLikes.value.toString())

            Column(modifier = Modifier
                .animateContentSize(animationSpec = tween(100))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { expanded.value = !expanded.value }) {

                if (expanded.value) {
                    Text(text = post.postDescription)
                } else {
                    Text(
                        text = post.postDescription,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Text("View all ${post.postComments.size} comments",
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
            PostModel("1", "Testing123", com.example.foodstabook.R.drawable.default_profile_photo,
            listOf(com.example.foodstabook.R.drawable.borscht, com.example.foodstabook.R.drawable.bunny_chow), SimpleDateFormat("26-01-2022"),
                context.getString(com.example.foodstabook.R.string.post_description_1), listOf("Borscht", "Beets"), 100,
                listOf(CommentsModel(com.example.foodstabook.R.drawable.default_profile_photo, "CommentTest",
                "Looks great!"))
            )
        )
    }
}