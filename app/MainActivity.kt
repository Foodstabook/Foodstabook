package com.example.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private var btnLogin:Button? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin = findViewById<Button>(R.id.btnlogin)


        btnLogin!!.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext,"Success",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@MainActivity,signin::class.java))
        })

    }
}