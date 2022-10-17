package com.example.foodstabook.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodstabook.R
import com.example.foodstabook.databinding.ActivityNewsfeedBinding
import com.example.foodstabook.model.CommentsModel
import com.example.foodstabook.model.NewsfeedAdapter
import com.example.foodstabook.model.PostModel
import kotlinx.android.synthetic.main.activity_newsfeed.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NewsfeedActivity : AppCompatActivity(), NewsfeedAdapter.OnViewCommentsClickListener {
    private lateinit var binding: ActivityNewsfeedBinding
    private val imageList = listOf(R.drawable.borscht, R.drawable.blueberry_crumble, R.drawable.bunny_chow,
        R.drawable.falafel, R.drawable.chicken_tikka_masala, R.drawable.ochazuke, R.drawable.kimchi_jjigae,
        R.drawable.shrimp_al_mojo_de_ajo, R.drawable.vegetable_terrine, R.drawable.ratatouille)
    private var postList = ArrayList<PostModel>()
    private val adapter = NewsfeedAdapter(postList, this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsfeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title = "Newsfeed"
        val recyclerview = binding.newsfeedrecyclerview

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        generatePostList()
        goHome()
        goToProfile()
    }

    private fun generatePostList() {
        postList.add(PostModel("1", "testing1", R.drawable.default_profile_photo, imageList.slice(0..3), SimpleDateFormat("26-01-2022", Locale.US),
            getString(R.string.post_description_1), generateTagsList1(), 100, generateCommentsList1()))

        postList.add(PostModel("2", "testing2", R.drawable.default_profile_photo, imageList.slice(4..6), SimpleDateFormat("27-01-2022", Locale.US),
            getString(R.string.post_description_2), generateTagsList2(), 64, generateCommentsList2()))

        postList.add(PostModel("3", "testing3", R.drawable.default_profile_photo, imageList.slice(7..7), SimpleDateFormat("28-01-2022", Locale.US),
            getString(R.string.post_description_3), generateTagsList3(), 41, generateCommentsList3()))

        postList.add(PostModel("4", "testing4", R.drawable.default_profile_photo, imageList.slice(8..9), SimpleDateFormat("29-01-2022", Locale.US),
            getString(R.string.post_description_4), generateTagsList4(),250, generateCommentsList4()))
        }

    private fun generateCommentsList1(): ArrayList<CommentsModel>{
        val comments = ArrayList<CommentsModel>()
        comments.add(CommentsModel(R.drawable.default_profile_photo, "TestCommenter1","Wow!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter2", "Looks tasty!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter3", "Not my cup of tea..."))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter4", ":3"))
        return comments
    }

    private fun generateCommentsList2(): ArrayList<CommentsModel>{
        val comments = ArrayList<CommentsModel>()
        comments.add(CommentsModel(R.drawable.default_profile_photo, "TestCommenter5","Wow!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter6", "Looks tasty!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter7", "Not my cup of tea..."))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter8", ":3"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter9", "Nice!"))
        return comments
    }

    private fun generateCommentsList3(): ArrayList<CommentsModel>{
        val comments = ArrayList<CommentsModel>()
        comments.add(CommentsModel(R.drawable.default_profile_photo, "TestCommenter10","Wow!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter11", "Looks tasty!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter12", "Not my cup of tea..."))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter13", ":3"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter14", "Nice!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter15", "Wish I could have eaten that!"))
        return comments
    }

    private fun generateCommentsList4(): ArrayList<CommentsModel>{
        val comments = ArrayList<CommentsModel>()
        comments.add(CommentsModel(R.drawable.default_profile_photo, "TestCommenter16","Wow!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter17", "Looks tasty!"))
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


    override fun onViewCommentsClick(position: Int) {
        val clickedPost = postList[position]
        val intent = Intent(this, CommentsActivity::class.java)
        intent.putExtra("clickedPost", clickedPost)
        startActivity(intent)
    }

    private fun goHome(){
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