package com.example.foodstabook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodstabook.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        // button to navigate to sign-in page
        binding.btnlogin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.btnsignup.setOnClickListener {
            val userName = binding.username.text.toString().trim()
            val firstName = binding.firstnameInput.text.toString().trim()
            val lastName = binding.lastnameInput.text.toString().trim()
            val email = binding.emailinput.text.toString().trim()
            val password = binding.passwordinput.toString().trim()
            val age = binding.ageInput.text.toString()
            val checkBox = binding.checkbox

            if(checkBox.isChecked) {
                if(inputValidation(userName, email, password, age)) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if(it.isSuccessful) {
                            val newUser = User(userName,firstName, lastName, email, age)
                            newUser.username = userName
                            newUser.firstname = firstName
                            newUser.lastname = lastName
                            newUser.email = email
                            newUser.age = age

                            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser!!.uid)
                                .setValue(newUser).addOnCompleteListener {
                                    if(it.isSuccessful) {
                                        Toast.makeText(this, "User added to database", Toast.LENGTH_LONG).show()
                                    }
                                }
                            // Everyting worked so send user to the sign-in page
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)

                        }else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    Toast.makeText(this,"Input validation failed", Toast.LENGTH_LONG).show()
                }
            }else {
                Toast.makeText(this, "Please agree to the terms and conditions by checking the box", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun inputValidation(userName: String?, email: String?, password: String?, age: String?): Boolean {
        if (userName == null || "".equals(userName)) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_LONG).show()
            return false;
        }
        if (age == null ) {
            Toast.makeText(this, "Please enter your age", Toast.LENGTH_LONG).show()
            return false
        }
        if (age.toInt() < 14) {
            Toast.makeText(this, "Sorry, only user aged 14 and older allowed", Toast.LENGTH_LONG)
                .show()
            return false
        }
        if (email == null || "".equals(email)) {
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_LONG).show()
            return false;
        }
        if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show()
            return false;
        }
        if (password == null || "".equals(password) || password.length < 6) {
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_LONG).show()
            return false;
        }
        return true
    }
}