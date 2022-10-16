package com.example.foodstabook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodstabook.databinding.ActivitySignInBinding

class LoginPage : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root     
        setContentView(view)
        goHome()
    }

    private fun goHome(){
        binding.smallLogo.setOnClickListener{
            finish()
        }
    }
}

