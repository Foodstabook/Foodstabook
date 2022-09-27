package com.example.foodstabook.activity

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        goHome()

        binding.profileImage.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            //startActivityForResult(gallery, pickImage)
        }

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }




}