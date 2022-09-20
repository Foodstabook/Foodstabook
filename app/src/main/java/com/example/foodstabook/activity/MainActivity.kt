package com.example.foodstabook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.foodstabook.*
import com.example.foodstabook.MainActivity
import com.example.foodstabook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //To force change the title of the activity
        title = "Main Menu"

        val button:Button = findViewById(R.id.profileButton)
        button.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        goToLogin()
        goToResetPassword()
        goToFoodSuggestion()
        goToUserAccount()
        goHome()

    }

    private fun goToLogin(){
        binding.loginButton.setOnClickListener{
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }

    private fun goToResetPassword(){
        binding.resetPasswordButton.setOnClickListener{
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }
    }

    private fun goToFoodSuggestion(){
        binding.foodSuggestionButton.setOnClickListener{
            val intent = Intent(this, SuggestionMainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToUserAccount(){
        binding.userAccountButton.setOnClickListener{
            val intent = Intent(this, UserAccount::class.java)
            startActivity(intent)
        }
    }

    private fun goHome(){
        binding.logo.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}