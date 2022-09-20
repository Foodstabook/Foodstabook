package com.example.foodstabook

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodstabook.databinding.ResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



class ResetPassword : AppCompatActivity() {

    // create instance of the ActivityMainBinding,
    // as we have only one layout activity_main.xml
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        binding = ResetPasswordBinding.inflate(layoutInflater);
        setContentView(binding.root);

        binding.btnResetPassword.setOnClickListener {
            val emailAddress = binding.edtResetEmail.toString()

            Firebase.auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email sent!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        Log.d(TAG, "Email sent.")
                    }
                }
        }
    }
}
