package com.example.foodstabook.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodstabook.R //Not used when we use binding
import com.example.foodstabook.databinding.ActivityProfileBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {


    private lateinit var binding : ActivityProfileBinding
    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_profile)
        setContentView(binding.root)


        binding.buttonUserOne.setOnClickListener{
            val userName : String = binding.displayProfileName.text.toString()
            val userEmail : String = binding.displayProfileEmail.text.toString()

            if(userName.isNotEmpty()){
                readData(userName)
            }else{
                Toast.makeText(this,"USERNAME ERROR",Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun readData(userName: String) {

        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(userName).get().addOnSuccessListener {  }

    }
}