package com.example.foodstabook.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodstabook.databinding.ActivityNewsfeedBinding
import com.example.foodstabook.model.NewsfeedAdapter
import com.example.foodstabook.model.PostModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_newsfeed.*
import java.util.*
import kotlin.collections.ArrayList


class NewsfeedActivity : AppCompatActivity(), NewsfeedAdapter.OnViewCommentsClickListener {
    private lateinit var binding: ActivityNewsfeedBinding
    private lateinit var postList : ArrayList<PostModel>
    private lateinit var adapter :NewsfeedAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsfeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title = "Newsfeed"
        val recyclerview = binding.newsfeedrecyclerview
        recyclerview.layoutManager = LinearLayoutManager(this)

        getPost()
        goHome()
        goToProfile()
    }

    private fun getPost() {
        postList = ArrayList()
        FirebaseFirestore.getInstance().collection("Post")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val posts = document.toObject(PostModel::class.java)
                    postList.add(posts)
                    Log.d("TAG", "${document.id} => ${document.data}")
                }
                showPosts(postList)
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }

    private fun showPosts(postList: ArrayList<PostModel>) {
        adapter = NewsfeedAdapter(postList,this)
        binding.newsfeedrecyclerview.adapter = adapter
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


}