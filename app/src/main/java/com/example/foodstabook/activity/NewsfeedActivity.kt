package com.example.foodstabook.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstabook.R
import com.example.foodstabook.databinding.ActivityNewsfeedBinding
import com.example.foodstabook.model.CommentsModel
import com.example.foodstabook.model.NewsfeedAdapter
import com.example.foodstabook.model.PostModel
import java.io.Serializable
import java.text.SimpleDateFormat


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
        val recyclerview = binding.newsfeedrecyclerview

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        generatePostList()
    }

    private fun generatePostList() {
        postList.add(PostModel("1", "testing1", R.drawable.default_profile_photo, imageList.slice(0..3), SimpleDateFormat("26-01-2022"),
            "Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah " +
                    "Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah" +
                    "Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah",
            100, generateCommentsList1()))

        postList.add(PostModel("2", "testing2", R.drawable.default_profile_photo, imageList.slice(4..6), SimpleDateFormat("27-01-2022"),
            "Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah " +
                    "Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah" +
                    "Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah",
            64, generateCommentsList2()))

        postList.add(PostModel("3", "testing3", R.drawable.default_profile_photo, imageList.slice(7..7), SimpleDateFormat("28-01-2022"),
            "Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah " +
                    "Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah" +
                    "Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah",
            41, generateCommentsList3()))

        postList.add(PostModel("4", "testing4", R.drawable.default_profile_photo, imageList.slice(8..9), SimpleDateFormat("29-01-2022"),
            "Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah " +
                    "Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah" +
                    "Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah",
            250, generateCommentsList4()))
        }

    private fun generateCommentsList1(): ArrayList<CommentsModel>{
        var comments = ArrayList<CommentsModel>()
        comments.add(CommentsModel(R.drawable.default_profile_photo, "TestCommenter1","Wow!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter2", "Looks tasty!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter3", "Not my cup of tea..."))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter4", ":3"))
        return comments
    }

    private fun generateCommentsList2(): ArrayList<CommentsModel>{
        var comments = ArrayList<CommentsModel>()
        comments.add(CommentsModel(R.drawable.default_profile_photo, "TestCommenter5","Wow!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter6", "Looks tasty!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter7", "Not my cup of tea..."))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter8", ":3"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter9", "Nice!"))
        return comments
    }

    private fun generateCommentsList3(): ArrayList<CommentsModel>{
        var comments = ArrayList<CommentsModel>()
        comments.add(CommentsModel(R.drawable.default_profile_photo, "TestCommenter10","Wow!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter11", "Looks tasty!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter12", "Not my cup of tea..."))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter13", ":3"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter14", "Nice!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter15", "Wish I could have eaten that!"))
        return comments
    }

    private fun generateCommentsList4(): ArrayList<CommentsModel>{
        var comments = ArrayList<CommentsModel>()
        comments.add(CommentsModel(R.drawable.default_profile_photo, "TestCommenter16","Wow!"))
        comments.add(CommentsModel(R.drawable.default_profile_photo,"TestCommenter17", "Looks tasty!"))
        return comments
    }

    override fun onViewCommentsClick(position: Int) {
        var clickedPost = postList[position]
        val intent = Intent(this, CommentsActivity::class.java)
        intent.putExtra("clickedPost", clickedPost)
        startActivity(intent)
    }
}