package com.example.foodstabook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var login: TextView? = null
    private var register: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        login = findViewById<View>(R.id.loginButton) as TextView
        login!!.setOnClickListener(this)
        register = findViewById<View>(R.id.registerButton) as TextView
        register!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginButton -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.registerButton -> startActivity(Intent(this, RegisterUserActivity::class.java))
        }
    }
}