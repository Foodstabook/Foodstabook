package com.example.foodstabook.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodstabook.databinding.ActivityProfileBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.content.Context

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding
    private lateinit var database : DatabaseReference


    //Creates the SharedPreferences Holder
    lateinit var sharedPreferences: SharedPreferences
    var PREFS_KEY = "prefs"
    var EMAIL_KEY = "email"
    var loggedEmail = ""


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        loggedEmail = sharedPreferences.getString(EMAIL_KEY, null)!!
        Toast.makeText(this,"Welcome \n$loggedEmail",Toast.LENGTH_SHORT).show()


        binding.buttonSearch.setOnClickListener{


            val userName : String = binding.textSearchUsernameProfile.text.toString()

            if(userName.isNotEmpty()){
                readData(userName)
            }else{
                Toast.makeText(this,"USERNAME ERROR",Toast.LENGTH_SHORT).show()
            }


        }

        binding.buttonAutoUserDummy.setOnClickListener {
            //This is a pre-set user.
            val dummyUserName = "Q4urHJQmhRcCTuQFyw2CvMQEAPr2"
            readData(dummyUserName)
        }

        binding.buttonClear.setOnClickListener{
            binding.textSearchUsernameProfile.setText("")
        }

        //This will clear our
        binding.buttonProfileLogOut.setOnClickListener{

            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            val i = Intent(this@ProfileActivity, LoginPage::class.java)
            startActivity(i)

            finish()
        }

        goHome()
    }

    private fun readData(uid: String) {

        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(uid).get().addOnSuccessListener {

            if(it.exists()){

                val firstName = it.child("firstname").value
                val lastName = it.child("lastname").value
                val name = firstName.toString() +" "+ lastName.toString()
                val id = it.child("username").value
                val email = it.child("email").value
                val age = it.child("age").value

                Toast.makeText(this,"Successfully Read",Toast.LENGTH_SHORT).show()

                binding.displayProfileName.text = name
                binding.displayProfileId.text = id.toString()
                binding.displayProfileEmail.text = email.toString()
                binding.displayProfileAge.text = age.toString()



            }else{
                Toast.makeText(this,"User don't exist",Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener{
            Toast.makeText(this,"Fail",Toast.LENGTH_SHORT).show()
        }
    }

    private fun goHome(){
        binding.logo.setOnClickListener{
            finish()
        }
    }
}