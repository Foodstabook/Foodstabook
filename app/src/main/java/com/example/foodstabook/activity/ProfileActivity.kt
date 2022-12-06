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
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding
    private lateinit var database : DatabaseReference
    private lateinit var functions: FirebaseFunctions

    //Creates the SharedPreferences Holder
    lateinit var sharedPreferences: SharedPreferences
    var PREFS_KEY = "prefs"
    var EMAIL_KEY = "email"
    var UID_KEY = "uid"
    var loggedEmail = ""
    var loggedUid = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // We instantiate the firebase functions object
        functions = Firebase.functions

        // We instantiate the binding object
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // We instantiate SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        loggedEmail = sharedPreferences.getString(EMAIL_KEY, null)!!
        loggedUid = sharedPreferences.getString(UID_KEY, null)!!
        Toast.makeText(this,"Welcome \n$loggedEmail",Toast.LENGTH_SHORT).show()

        readData(loggedUid)

        binding.buttonSearch.setOnClickListener{

            callAddMessage("Test from android")

            val userName : String = binding.textSearchUsernameProfile.text.toString()

            if(userName.isNotEmpty()) readData(userName)
            else Toast.makeText(this,"USERNAME ERROR",Toast.LENGTH_SHORT).show()

        }

        //Will automatically populate a preset user information
        binding.buttonAutoUserDummy.setOnClickListener {
            //This is a pre-set user.
            val dummyUserName = "Q4urHJQmhRcCTuQFyw2CvMQEAPr2"
            readData(dummyUserName)
        }
        //We clear the text fields
        binding.buttonClear.setOnClickListener{
            binding.textSearchUsernameProfile.setText("")
        }
        //This will clear our SharedPreferences
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

    //Gets the personal data from our database, using the native firebase auth
    //TODO convert into cloud function
    private fun readData(uid: String) {

        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(uid).get().addOnSuccessListener {

            if(it.exists()){

                val firstName = it.child("firstName").value
                val lastName = it.child("lastName").value
                val name = firstName.toString() +" "+ lastName.toString()
                val id = it.child("userName").value
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

    //Function that gets values from a firestore function
    private fun getValuesFromFunction() {
        val functions = FirebaseFunctions.getInstance()
        functions.getHttpsCallable("getValues")
            .call()
            .addOnSuccessListener { result ->
                val data = result.data as Map<*, *>
                val value1 = data["value1"] as String
                val value2 = data["value2"] as String
                Toast.makeText(this, "Value1: $value1, Value2: $value2", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                if (e is IOException) {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Unknown error", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //
    private fun getValuesFromFirestoreFunction() {
        val data = hashMapOf(
            "name" to "John Doe",
            "age" to 30
        )

        val function = FirebaseFunctions.getInstance("us-central1")
        function
            .getHttpsCallable("kevinAddMessage")
            .call(data)
            .addOnSuccessListener { result ->
                val data = result.data as Map<*, *>
                val message = data["message"] as String
                Toast.makeText(this, message+"SUCCESS", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message+"FAIL", Toast.LENGTH_LONG).show()
            }
    }

    //Function that uses a firestore function to change the user profile picture in the database
    private fun changeProfilePicture() {
        val functions = FirebaseFunctions.getInstance()
        functions.getHttpsCallable("changeProfilePicture")
            .call()
            .addOnSuccessListener { result ->
                val data = result.data as Map<*, *>
                val message = data["message"] as String
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                if (e is IOException) {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Unknown error", Toast.LENGTH_SHORT).show()
                }
            }
    }


    //Function that  receives a timestamp from a firestore http function
    private fun getTimestampFromFunction() {



        val functions = FirebaseFunctions.getInstance()
        functions.getHttpsCallable("timeStamp")
            .call()
            .addOnSuccessListener { result ->
                val data = result.data as Map<*, *>
                val timestamp = data["timestamp"] as Long
                val date = Date(timestamp)
                val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                Toast.makeText(this, format.format(date), Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                if (e is IOException) {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Unknown error2", Toast.LENGTH_SHORT).show()
                }
            }
    }


    //Function that sends a string to a firestore function
    private fun sendStringToFunction() {
        val data = hashMapOf(
            "name" to "John Doe",
            "age" to 30
        )

        val functions = FirebaseFunctions.getInstance()
        functions.getHttpsCallable("helloWorld")
            .call(data)
            .addOnSuccessListener { result ->
                val data = result.data as Map<*, *>
                val message = data["message"] as String
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                if (e is IOException) {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Unknown error", Toast.LENGTH_SHORT).show()
                }
            }
    }




    private fun addMessage(text: String): Task<String> {
        // Create the arguments to the callable function.
        val data = hashMapOf(
            "text" to text,
            "push" to true
        )

        return functions
            .getHttpsCallable("addMessage")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as String
                result
            }
    }

    private fun callAddMessage(inputMessage: String){

        addMessage(inputMessage)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val e = task.exception
                    if (e is FirebaseFunctionsException) {
                        val code = e.code
                        val details = e.details

                        Toast.makeText(this, "Error: $code $details", Toast.LENGTH_SHORT).show()

                    }
                }
            }

    }


    //Function that requests a http function
    private fun requestHttpFunction() {
        val functions = FirebaseFunctions.getInstance()
        functions.getHttpsCallable("helloWorld")
            .call()
            .addOnSuccessListener { result ->
                val data = result.data as Map<*, *>
                val message = data["message"] as String
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                if (e is IOException) {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Unknown error", Toast.LENGTH_SHORT).show()
                }
            }
    }
}