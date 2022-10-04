package com.example.foodstabook.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foodstabook.databinding.ActivityProfileBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {

    companion object{
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 0
        private val REQUEST_TAKE_PHOTO = 1
    }

    private lateinit var binding : ActivityProfileBinding
    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setContentView(R.layout.activity_profile)


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

            //selectSourcePicture(buttonProfileLogOut)
            //takePhoto()
            selectImageInAlbum()
        }

    }

    //Pulls information from a database with the UID
    private fun readData(uid: String) {

        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(uid).get().addOnSuccessListener {

            if(it.exists()){

                Toast.makeText(this,"User found!",Toast.LENGTH_SHORT).show()

                val firstName = it.child("firstname").value
                val lastName = it.child("lastname").value
                val name = firstName.toString() +" "+ lastName.toString()
                val id = it.child("username").value
                val email = it.child("email").value
                val age = it.child("age").value

                binding.displayProfileName.text = name
                binding.displayProfileId.text = id.toString()
                binding.displayProfileEmail.text = email.toString()
                binding.displayProfileAge.text = age.toString()

            }
            else{
                Toast.makeText(this,"Info about user not found",Toast.LENGTH_SHORT).show()
            }


        }.addOnFailureListener{
            Toast.makeText(this,"Fail to get Data",Toast.LENGTH_SHORT).show()
        }
    }

    //Moves the activity to another
    private fun goHome(){
        binding.logo.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    ///////////////////////////////////////////////////////////////////

    // Takes a new photo
    fun takePhoto() {
        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent1.resolveActivity(packageManager) != null) {
            startActivityForResult(intent1, REQUEST_TAKE_PHOTO)
        }
    }

    //Select an Image from the album
    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }


    //
    fun selectSourcePicture(view: View) {

        val cancelButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(applicationContext, android.R.string.cancel, Toast.LENGTH_SHORT).show()
        }

        val items = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle("Get an Image from :")
            setItems(items) { dialog, which ->
                Toast.makeText(applicationContext, items[which] + " is clicked", Toast.LENGTH_SHORT).show()
            }

            setPositiveButton("Cancel", cancelButtonClick)
            show()
        }
    }




}