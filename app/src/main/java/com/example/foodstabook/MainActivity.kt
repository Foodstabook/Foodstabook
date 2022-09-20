package com.example.foodstabook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodstabook.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        //checkLogin()

        binding.btnRegistration.setOnClickListener{
            registerUser()
        }
        binding.btnLogin.setOnClickListener{
            UserLogin()
        }
    }

    private fun checkLogin(){
        if(user.currentUser != null){
            startActivity(Intent(this,SettingActivity::class.java))
            finish()
        }
    }

    private  fun UserLogin(){
        val email = binding.etEmailAddress.text.toString()
        val password = binding.etPassword.text.toString()

        if(email.isNotEmpty() && password.isNotBlank()){
            user.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(MainActivity()) {task ->
                    if(task.isSuccessful){
                        Toast.makeText(this,
                            "Sign in successfully",
                            Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, SettingActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this, task.exception!!.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }else{
            Toast.makeText(this,
                "Email and Password can not be empty",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerUser(){
        val email = binding.etEmailAddress.text.toString()
        val password = binding.etPassword.text.toString()

        if(email.isNotEmpty() && password.isNotBlank()){
            user.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(MainActivity()) {task ->
                    if(task.isSuccessful){
                        Toast.makeText(this,
                            "User added successfully",
                            Toast.LENGTH_SHORT).show()
                    }else{
                        // this line might help
//                        Toast.makeText(this, task.exception.toString(),
//                            Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, task.exception!!.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }else{
            Toast.makeText(this,
                "Email and Password can not be empty",
                Toast.LENGTH_SHORT).show()
        }
    }

}