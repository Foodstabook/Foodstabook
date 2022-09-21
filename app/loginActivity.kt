package com.example.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.view.View
import android.widget.*


import com.example.signin.databinding.ActivityLoginBinding

class loginActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val loginButton: TextView = findViewById(R.id.btnlogin)
        loginButton.setOnClickListener(
            doLogin()
        )
    }

    private fun doLogin(): View.OnClickListener? {
        val email:EditText = findViewById(R.id.emailinput)
        val password:EditText = findViewById(R.id.passwordinput)

        if(email.text.isEmpty()||password.text.isEmpty()){
            Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show()
            return
        }
        val userEmail = email.text.toString()
        val userPassword = password.text.toString()

        auth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(this) {
            task ->
            if(task.isSuccessful){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                Toast.makeText(baseContext, "Success",Toast.LENGTH_SHORT).show()


            }else{
                Toast.makeText(baseContext,"Authentication failed.",Toast.LENGTH_SHORT).show()

            }
        }
    }

}

/Users/dishant/AndroidStudioProjects/signin/app/src/main/java/com/example/signin/loginActivity.kt