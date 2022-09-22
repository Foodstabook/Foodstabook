package com.example.signin

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


class signin : AppCompatActivity() {
    val mAuth  = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)


        val login = findViewById<Button>(R.id.btnlogin) as Button
        //val signupTxt = findViewById<View>(R.id.btnsignup) as TextView

        login.setOnClickListener(View.OnClickListener {
                view-> loginFunction ()

        })

    }
    private fun loginFunction() {
        val email = findViewById<EditText>(R.id.emailinput) as EditText
        val password = findViewById<EditText>(R.id.passwordinput) as EditText

        var useremail = email.text.toString()
        var userpassword = password.text.toString()

        if (!useremail.isEmpty() && !userpassword.isEmpty()){
            mAuth.signInWithEmailAndPassword(useremail,userpassword).addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful){
                    startActivity(Intent(this@signin, MainActivity::class.java))
                    Toast.makeText(this@signin,"successfully Login",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@signin,"error ",Toast.LENGTH_SHORT).show()
                }
            })

        }else{
            Toast.makeText(this@signin,"Please Enter your Email or Passsword" ,Toast.LENGTH_SHORT).show()
        }
    }
}