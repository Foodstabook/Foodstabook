package com.example.foodstabook.activity

import android.content.Intent
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
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        val view = binding.root
        val post = intent.getParcelableExtra<PostModel>("clickedPost")
        setContentView(view)

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
            val intent = Intent(this, NewsfeedActivity::class.java)
            startActivity(intent)
        }
    }
}