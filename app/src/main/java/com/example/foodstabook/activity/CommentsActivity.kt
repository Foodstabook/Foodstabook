package com.example.foodstabook.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodstabook.databinding.ActivityCommentsBinding
import com.example.foodstabook.model.CommentsAdapter
import com.example.foodstabook.model.CommentsModel
import com.example.foodstabook.model.PostModel

class CommentsActivity:AppCompatActivity() {
    private lateinit var binding: ActivityCommentsBinding
    private var commentList = ArrayList<CommentsModel>()
    private val adapter = CommentsAdapter(commentList)
    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        this.supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        val view = binding.root
        val post = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("clickedPost", PostModel::class.java)
        } else {
            intent.getParcelableExtra("clickedPost")
        }
        setContentView(view)
        title = "Comments"

        if (post != null) {
            for (i in 1..post.postComments.size)
                commentList.add((post.postComments[i-1]))
        }
        val recyclerview = binding.commentsrecyclerview
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)
        returnToNewsfeed()
    }

    private fun returnToNewsfeed(){
        binding.backToNewsfeed.setOnClickListener{
            finish()
        }
    }
}