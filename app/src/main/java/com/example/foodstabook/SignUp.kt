package com.example.foodstabook

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class SignUp : AppCompatActivity(), View.OnClickListener {

    private var userNameEdt: EditText? = null
    private var ageEdt: EditText? = null // Update to getting user's date of birth?
    private var emailEdt: EditText? = null
    private var passwordEdt: EditText? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        userNameEdt = findViewById(R.id.username)
        ageEdt = findViewById(R.id.age)
        emailEdt = findViewById(R.id.email)
        passwordEdt = findViewById(R.id.password)
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.registerUser -> registerUser()
        }
    }

    private fun registerUser() {
        var isValidInput = inputValidation(userNameEdt.toString(), emailEdt.toString(), passwordEdt.toString(),ageEdt.toString())
        if (isValidInput == true) {
            auth.createUserWithEmailAndPassword(emailEdt.toString(), passwordEdt.toString())
                .addOnCompleteListener { task ->
                    val user = User(userNameEdt.toString(), ageEdt.toString(), emailEdt.toString())

                    FirebaseDatabase.getInstance().getReference("users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(user).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "User registered successfully",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else Toast.makeText(
                                this,
                                "Failed to register user1",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
        }
    }

    private fun inputValidation(userName: String?, email: String?, password: String?, age: String?): Boolean {
        if (userName == null || "".equals(userName)) {
            Toast.makeText(this@SignUp, "Please enter a username", Toast.LENGTH_LONG).show()
            return false;
        }
        if (age == null ) {
            Toast.makeText(this@SignUp, "Please enter your age", Toast.LENGTH_LONG).show()
            return false
        }
        if (age != null) {
            if (age.toInt() < 14)
                Toast.makeText(this@SignUp, "Sorry, only user aged 14 and older allowed", Toast.LENGTH_LONG).show()
            return false
        }
        if (email == null || "".equals(email)) {
            Toast.makeText(this@SignUp, "Please enter an email address", Toast.LENGTH_LONG).show()
            return false;
        }
        if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(this@SignUp, "Please enter a valid email address", Toast.LENGTH_LONG).show()
            return false;
        }
        if (password == null || "".equals(password) || password.length < 6) {
            Toast.makeText(this@SignUp, "Please enter an email address", Toast.LENGTH_LONG).show()
            return false;
        }
        return true
    }

}