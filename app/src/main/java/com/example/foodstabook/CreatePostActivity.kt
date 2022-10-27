package com.example.foodstabook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodstabook.activity.MainActivity
import com.example.foodstabook.databinding.ActivityCreatePostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePostActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCreatePostBinding
    private lateinit var user: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var referance: DatabaseReference
    //cloud function
    private lateinit var functions: FirebaseFunctions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        referance = Firebase.database.reference
        functions = Firebase.functions

        val actionbar = supportActionBar
        actionbar!!.title = "Create Post"
        actionbar.setDisplayHomeAsUpEnabled(true)

        //Rating Bar change value
        rbRating.setOnRatingBarChangeListener { rbRating, f1, b -> Rating.setText("Rating: ") }

        binding.btnpost.setOnClickListener {
            if (user.currentUser != null) {
                user.currentUser?.let {
                    //create post
                    CreatePost3(it.uid)
                }
            }
        }

        //Cancel button
        binding.btnCancelpost.setOnClickListener {
            startActivity(Intent(this, CreatePostActivity::class.java))
        }
    }

    private fun CreatePost3(uid: String){
        referance = FirebaseDatabase.getInstance().getReference("users")
        referance.child(uid).get().addOnSuccessListener {
            if(it.exists()){
                //get the current username from the real time database
                val username = it.child("username").value.toString()
                //call the CreatePost function and pass username from real time database
                CreatePost2(uid,username)
            }else{
                Toast.makeText(this,"User don't exist",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Fail",Toast.LENGTH_SHORT).show()
        }
    }

    //create post and save data to cloud database
    private fun CreatePost2(uid: String, username: String) {

        val title = binding.title.text.toString()
        val hashtags = binding.hashtags.text.toString()
        val hashtagList = hashtags.split(",").toTypedArray()

        val place = binding.place.text.toString()
        val rating = binding.rbRating.getRating()
        val description = binding.description.text.toString()

        if(inputValidation(title, hashtags, place, rating, description)) {
            val posts: MutableMap<String, Any> = HashMap()
            posts["Title"] = title
            posts["Place"] = place
            posts["Rating"] = rating
            posts["Description"] = description
            posts["UID"] = uid
            posts["Username"] = username

            for ((index, h) in hashtagList.withIndex()) {
                posts["Hashtag$index"] = h
            }



            db.collection("Post").add(posts).addOnSuccessListener {
                Toast.makeText(this, "Post Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }.addOnFailureListener {
                Toast.makeText(this, "Post Failure.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inputValidation(title: String?, hashtags: String?, place: String?, rating: Float, description: String?): Boolean  {
        if (title == null || "".equals(title)) {
            Toast.makeText(this, "Please enter a Title", Toast.LENGTH_LONG).show()
            return false;
        } else if(title.length>20){
            Toast.makeText(this, "Title no more than 20 characters.", Toast.LENGTH_LONG).show()
            return false;
        }

        if (hashtags == null || "".equals(hashtags)) {
            Toast.makeText(this, "Please enter Hashtag", Toast.LENGTH_LONG).show()
            return false;
        }else if(hashtags.length>30){
            Toast.makeText(this, "Hashtag no more than 20 characters.", Toast.LENGTH_LONG).show()
            return false;
        }

        if (place == null || "".equals(place)) {
            Toast.makeText(this, "Please enter Place", Toast.LENGTH_LONG).show()
            return false;
        }else if(place.length>20){
            Toast.makeText(this, "Place no more than 20 characters.", Toast.LENGTH_LONG).show()
            return false;
        }

        if (rating <= 0) {
            Toast.makeText(this, "Please Rate", Toast.LENGTH_LONG).show()
            return false;
        }

        if (description == null || "".equals(description) ) {
            Toast.makeText(this, "Please enter Description", Toast.LENGTH_LONG).show()
            return false;
        }else if(description.length>100){
            Toast.makeText(this, "Description no more than 100 characters.", Toast.LENGTH_LONG).show()
            return false;
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
