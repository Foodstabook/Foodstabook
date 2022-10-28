package com.example.foodstabook.activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.foodstabook.R
import com.example.foodstabook.model.User
import com.example.foodstabook.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
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

        binding.tos.text = createCheckBoxText()
        binding.tos.movementMethod = LinkMovementMethod.getInstance()
        // button to navigate to sign-in page
        binding.btnlogin.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        binding.btnsignup.setOnClickListener {
            val userName = binding.username.text.toString().trim()
            val firstName = binding.firstnameInput.text.toString().trim()
            val lastName = binding.lastnameInput.text.toString().trim()
            val email = binding.emailinput.text.toString().trim()
            val password = binding.passwordinput.text.toString().trim()
            val age = binding.ageInput.text.toString()
            val checkBox = binding.checkbox

            if(checkBox.isChecked) {
                if(inputValidation(userName, email, password, age)) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { it ->
                        if(it.isSuccessful) {
                            val newUser = User(userName,firstName, lastName, email, age)
                            newUser.userName = userName
                            newUser.firstName = firstName
                            newUser.lastName = lastName
                            newUser.email = email
                            newUser.age = age

                            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser!!.uid)
                                .setValue(newUser).addOnCompleteListener {
                                    if(it.isSuccessful) {
                                        Toast.makeText(this, "User added to database", Toast.LENGTH_LONG).show()
                                    }
                                }
                            // Everything worked, notify user that they must verify their email
                            // and send them to the login page
                            emailverificationAlert()
                        }else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }else {
                Toast.makeText(this, "Please agree to the terms and conditions by checking the box", Toast.LENGTH_LONG).show()
            }
        }
        goHome()
        createCheckBoxText()
    }

    private fun inputValidation(userName: String?, email: String?, password: String?, age: String?): Boolean {
        if (userName == null || "" == userName) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_LONG).show()
            return false
        }
        if (userName.length > 32) {
            Toast.makeText(this, "Your username must be less than 33 characters", Toast.LENGTH_LONG).show()
            return false
        }
        if (email?.let { isValidEmail(it) } == false){
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_LONG).show()
            return false
        }
        if (age == null ) {
            Toast.makeText(this, "Please enter your age", Toast.LENGTH_LONG).show()
            return false
        }
        if (age.toInt() < 14) {
            Toast.makeText(this, "Sorry, only users age 14 and older are allowed", Toast.LENGTH_LONG)
                .show()
            return false
        }
        if (email == null || "" == email) {
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_LONG).show()
            return false
        }
        if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show()
            return false
        }
        if (password == null || "" == password || password.length < 6) {
            Toast.makeText(this, "Your password must be at least 6 characters long", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun goHome(){
        binding.logo.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createCheckBoxText(): SpannableString {
        val checkBoxText = SpannableString("I Agree to the terms and conditions.")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                TOSAlert()
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.RED
                ds.isUnderlineText = false
            }
        }

        checkBoxText.setSpan(clickableSpan, 15, checkBoxText.length-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return checkBoxText
    }
    private fun TOSAlert() {
        val tosDialog = AlertDialog.Builder(this)
        tosDialog.setTitle("Terms and Conditions")
        tosDialog.setNeutralButton("Close"){ _, _ ->
        }
        tosDialog.setMessage(R.string.terms_of_service)
        tosDialog.show()
    }

    private fun emailverificationAlert() {
        val intent = Intent(this, LoginPage::class.java)
        val verificationDialog = AlertDialog.Builder(this)
        verificationDialog.setTitle("Please verify your email.")
        verificationDialog.setNeutralButton("Go to Login", DialogInterface.OnClickListener{
            dialog, which ->  startActivity(intent)
        })
        verificationDialog.setMessage("An email has been sent to the account you registered with, please " +
                "click the link inside to verify your email.")
        verificationDialog.show()
    }

    private fun isValidEmail(email: String): Boolean {
        return email.let { android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() }
    }
}