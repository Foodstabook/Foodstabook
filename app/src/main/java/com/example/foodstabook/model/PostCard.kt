package com.example.foodstabook.model

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodstabook.activity.CommentsActivity
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
    val backArrowVisible = remember { mutableStateOf(false)}
    val forwardArrowVisible = remember { mutableStateOf(true)}
    val tags = getTags(post)
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
                Image(painter = painterResource(id = post.authorIcon),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(60.dp)
                        .background(color = Color.Transparent)
                        .offset(x = 4.dp)
                        .clickable { }
                )
                Text(post.postAuthor,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .offset(x = 6.dp)
                        .clickable { })
            }
            Box() {
                Image(painter = painterResource(id = post.postImage[imagePosition.value]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .aspectRatio(1f)
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
                if (imagePosition.value != 0) {
                    if (backArrowVisible.value) {
                        Image(painter = painterResource(id = com.example.foodstabook.R.drawable.back_arrow),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .size(36.dp)
                                .alpha(0.7f)
                                .clickable {
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
                            Image(painter = painterResource(id = com.example.foodstabook.R.drawable.forward_arrow),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .size(36.dp)
                                    .alpha(0.7f)
                                    .clickable {
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
                modifier = Modifier.offset(x = 4.dp)
            )
            Text("Likes: " + numLikes.value.toString(),
                color = Color.Gray,
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.offset(x = 4.dp))

            Column(modifier = Modifier
                .animateContentSize(animationSpec = tween(100))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { expanded.value = !expanded.value }) {

                if (expanded.value) {
                    Text(buildAnnotatedString {
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
                        val intent = Intent(context, CommentsActivity::class.java)
                        intent.putExtra("clickedPost", post)
                        context.startActivity(intent)
                    }
                    .offset(4.dp))
        }
    }
}

private fun getTags(post: PostModel): String {
    var tags = ""

    for (i in post.postTags) {
        var tag = "#" + i.filter { !it.isWhitespace() } + " "
        tags += tag.lowercase()
    }
    return tags
}

@Composable
@Preview(showBackground = true)
fun postPreview() {
    val context = LocalContext.current
    MaterialTheme {
        PostCard(
            PostModel("1", "Testing123", com.example.foodstabook.R.drawable.default_profile_photo,
            "", listOf(com.example.foodstabook.R.drawable.borscht, com.example.foodstabook.R.drawable.bunny_chow), SimpleDateFormat("26-01-2022"),
                context.getString(com.example.foodstabook.R.string.post_description_1), listOf("Borscht", "Beets"), 100,
                listOf(CommentsModel(com.example.foodstabook.R.drawable.default_profile_photo, "CommentTest",
                "Looks great!"))
            )
        )
    }
}