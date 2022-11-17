package com.example.foodstabook.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodstabook.activity.MainActivity
import com.example.foodstabook.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class LoginPage : AppCompatActivity() {
    private lateinit var user: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding


    //Saving the user SharedPreferences:
    lateinit var sharedPreferences: SharedPreferences

    var PREFS_KEY = "prefs"
    var EMAIL_KEY = "email"
    var PWD_KEY = "pwd"

    var loggedEmail = ""
    var loggedPwd = ""


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        loggedEmail = sharedPreferences.getString(EMAIL_KEY, "").toString()
        loggedPwd = sharedPreferences.getString(PWD_KEY, "").toString()


        user = FirebaseAuth.getInstance()
        goHome()

        binding.btnlogin.setOnClickListener{
            UserLogin()
        }

        binding.btnforgotpass.setOnClickListener{
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }

        binding.btnsignup.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        if (!loggedEmail.equals("") && !loggedPwd.equals("")) {

            val i = Intent(this@LoginPage, ProfileActivity::class.java)
            startActivity(i)
            finish()
        }else{
            Toast.makeText(this,
                "Shared pref Empty",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun goHome(){
        binding.logo.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private  fun UserLogin(){
        val email = binding.emailinput.text.toString()
        val password = binding.passwordinput.text.toString()

        if(email.isNotEmpty() && password.isNotBlank()){



            user.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(MainActivity()) {task ->
                    if(task.isSuccessful){


                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString(EMAIL_KEY, email)
                        editor.putString(PWD_KEY, password)

                        editor.apply()

                        Toast.makeText(this,
                            "Sign in successfully",
                            Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
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
}


