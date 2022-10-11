package com.example.foodstabook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodstabook.databinding.ActivityUserAccountBinding
import com.example.foodstabook.activity.MainActivity

class UserAccount : AppCompatActivity() {
    private lateinit var binding: ActivityUserAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        goHome()
    }

    private fun goHome(){
        binding.logo.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}