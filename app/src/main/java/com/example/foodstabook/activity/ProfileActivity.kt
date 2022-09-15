package com.example.foodstabook.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodstabook.R //Not used when we use binding
import com.example.foodstabook.databinding.ActivityProfileBinding
import com.google.firebase.database.DatabaseReference

class ProfileActivity : AppCompatActivity() {


    private lateinit var binding : ActivityProfileBinding
    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_profile)
        setContentView(binding.root)


        binding.buttonUserOne.setOnClickListener{
            val userName : String = binding.displayProfileName.text.toString()
            val userEmail : String = binding.displayProfileEmail.text.toString()
        }
    }
}