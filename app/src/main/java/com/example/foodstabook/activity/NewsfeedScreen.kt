package com.example.foodstabook.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SearchView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.foodstabook.R
import com.example.foodstabook.model.PostCard
import com.example.foodstabook.databinding.ActivityNewsfeedBinding
import com.example.foodstabook.model.CommentsModel
import com.example.foodstabook.model.NewsfeedAdapter
import com.example.foodstabook.model.PostModel
import kotlinx.android.synthetic.main.activity_newsfeed.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private var postList = ArrayList<PostModel>()
private val imageList = listOf(R.drawable.borscht, R.drawable.blueberry_crumble, R.drawable.bunny_chow,
    R.drawable.falafel, R.drawable.chicken_tikka_masala, R.drawable.ochazuke, R.drawable.kimchi_jjigae,
    R.drawable.shrimp_al_mojo_de_ajo, R.drawable.vegetable_terrine, R.drawable.ratatouille)
class NewsfeedActivity : AppCompatActivity(), NewsfeedAdapter.OnViewCommentsClickListener {
    private lateinit var binding: ActivityNewsfeedBinding
    private val adapter = NewsfeedAdapter(postList, this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            lazyNewsfeed(posts = postList)
        }
    }

    override fun onViewCommentsClick(position: Int) {
        val clickedPost = postList[position]
        val intent = Intent(this, CommentsActivity::class.java)
        intent.putExtra("clickedPost", clickedPost)
        startActivity(intent)
    }

    fun goHome(){
        binding.logo.setOnClickListener{
            finish()
        }
    }

    private fun goToProfile(){
        binding.profileImage.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        search_bar.clearFocus()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) {
            if(ev.action == MotionEvent.ACTION_DOWN){
                val focus = currentFocus
                if(focus is EditText ){
                    var outRect = Rect()
                    focus.getGlobalVisibleRect(outRect)
                    if(!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())){
                        focus.clearFocus()
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(focus.windowToken, 0)
                    }
                }
            }

        }
        return super.dispatchTouchEvent(ev)
    }

}

fun generatePostList(context: Context) {
    postList.add(PostModel("1", "testing1", R.drawable.foodstabook_logo4, "A Smorgasbord of Suggestions", imageList.slice(0..3), SimpleDateFormat("26-01-2022", Locale.US),
        context.getString(R.string.post_description_1), generateTagsList1(), 100, generateCommentsList1()))

    postList.add(PostModel("2", "testing2", R.drawable.foodstabook_logo4, "Some of My Favorite Dishes from My Recent Trip to Asia:", imageList.slice(4..6), SimpleDateFormat("27-01-2022", Locale.US),
        context.getString(R.string.post_description_2), generateTagsList2(), 64, generateCommentsList2()))

    postList.add(PostModel("3", "testing3", R.drawable.foodstabook_logo4, "Authentic Shrimp al Mojo de Ajo!", imageList.slice(7..7), SimpleDateFormat("28-01-2022", Locale.US),
        context.getString(R.string.post_description_3), generateTagsList3(), 41, generateCommentsList3()))

    postList.add(PostModel("4", "testing4", R.drawable.foodstabook_logo4, "French Cuisine: Some Classics",imageList.slice(8..9), SimpleDateFormat("29-01-2022", Locale.US),
        context.getString(R.string.post_description_4), generateTagsList4(),250, generateCommentsList4()))
}

private fun generateCommentsList1(): ArrayList<CommentsModel>{
    val comments = ArrayList<CommentsModel>()
    comments.add(CommentsModel(R.drawable.foodstabook_logo4, "TestCommenter1","Wow!"))
    comments.add(CommentsModel(R.drawable.foodstabook_logo4,"TestCommenter2", "Looks tasty!"))
    comments.add(CommentsModel(R.drawable.foodstabook_logo4,"TestCommenter3", "Not my cup of tea..."))
    comments.add(CommentsModel(R.drawable.foodstabook_logo4,"TestCommenter4", ":3"))
    return comments
}

private fun generateCommentsList2(): ArrayList<CommentsModel>{
    val comments = ArrayList<CommentsModel>()
    comments.add(CommentsModel(R.drawable.foodstabook_logo4, "TestCommenter5","Wow!"))
    comments.add(CommentsModel(R.drawable.foodstabook_logo4,"TestCommenter6", "Looks tasty!"))
    comments.add(CommentsModel(R.drawable.foodstabook_logo4,"TestCommenter7", "Not my cup of tea..."))
    comments.add(CommentsModel(R.drawable.foodstabook_logo4,"TestCommenter8", ":3"))
    comments.add(CommentsModel(R.drawable.foodstabook_logo4,"TestCommenter9", "Nice!"))
    return comments
}

private fun generateCommentsList3(): ArrayList<CommentsModel>{
    val comments = ArrayList<CommentsModel>()
    comments.add(CommentsModel(R.drawable.foodstabook_logo4, "TestCommenter10","Wow!"))
    comments.add(CommentsModel(R.drawable.foodstabook_logo4,"TestCommenter11", "Looks tasty!"))
    comments.add(CommentsModel(R.drawable.foodstabook_logo4,"TestCommenter12", "Not my cup of tea..."))
    comments.add(CommentsModel(R.drawable.foodstabook_logo4,"TestCommenter13", ":3"))
    comments.add(CommentsModel(R.drawable.foodstabook_logo4,"TestCommenter14", "Nice!"))
    comments.add(CommentsModel(R.drawable.foodstabook_logo4,"TestCommenter15", "Wish I could have eaten that!"))
    return comments
}

private fun generateCommentsList4(): ArrayList<CommentsModel>{
    val comments = ArrayList<CommentsModel>()
    comments.add(CommentsModel(R.drawable.foodstabook_logo4, "TestCommenter16","Wow!"))
    comments.add(CommentsModel(R.drawable.foodstabook_logo4,"TestCommenter17", "Looks tasty!"))
    return comments
}

private fun generateTagsList1(): ArrayList<String>{
    val tags = ArrayList<String>()
    tags.add("Borscht")
    tags.add("Pie")
    tags.add("Dessert")
    tags.add("African")
    tags.add("Bunny Chow")
    tags.add("Falafel")
    return tags
}

private fun generateTagsList2(): ArrayList<String>{
    val tags = ArrayList<String>()
    tags.add("Indian")
    tags.add("Chicken")
    tags.add("Tea")
    tags.add("Rice")
    tags.add("Japanese")
    tags.add("Korean")
    tags.add("Spicy")
    return tags
}

private fun generateTagsList3(): ArrayList<String>{
    val tags = ArrayList<String>()
    tags.add("Mexican")
    tags.add("Garlic")
    tags.add("Shrimp")
    return tags
}

private fun generateTagsList4(): ArrayList<String>{
    val tags = ArrayList<String>()
    tags.add("French")
    tags.add("Veggies")
    tags.add("Terrine")
    tags.add("Ratatouille")
    return tags
}

@Composable
fun lazyNewsfeed(posts: List<PostModel>) {
    val context = LocalContext.current
    Column() {


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            AndroidView(
                factory = { xmlContext ->
                    SearchView(xmlContext).apply {
                        queryHint = "Search"
                    }
                },
                modifier = Modifier.padding(end = 50.dp)
            )
            Image(painter = painterResource(id = R.drawable.default_profile_photo),
                contentDescription = null,
                alignment = Alignment.CenterEnd,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(48.dp)
                    .clickable {
                        val intent = Intent(context, ProfileActivity::class.java)
                        context.startActivity(intent)
                    }
            )
        }
        LazyColumn(modifier = Modifier.fillMaxWidth(1f),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(posts) { post ->
                PostCard(post)
            }
        }
    }
}

@Composable
@Preview
fun newsfeedPreview(){
    generatePostList(LocalContext.current)
    MaterialTheme{
        lazyNewsfeed(posts = postList)
    }
}