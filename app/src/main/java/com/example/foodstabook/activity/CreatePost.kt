package com.example.foodstabook.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.foodstabook.databinding.ActivityCreatePostBinding
import com.example.foodstabook.model.PostModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_create_post.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


open class CreatePost : AppCompatActivity() {
    private lateinit var binding:ActivityCreatePostBinding
    private lateinit var user: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var referance: DatabaseReference
    private val PICK_IMAGE = 1
    private var ImageUri: Uri? = null
    private var upload_count = 0
    private var progressDialog: ProgressDialog? = null
    private var imageList = ArrayList<Uri>()
    private lateinit var urlString : ArrayList<String>
    //cloud function
    private lateinit var functions: FirebaseFunctions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        referance = Firebase.database.reference
        functions = Firebase.functions
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage(" Please Wait");
        val actionbar = supportActionBar
        actionbar!!.title = "Create Post"
        actionbar.setDisplayHomeAsUpEnabled(true)

        //Rating Bar change value
        rbRating.setOnRatingBarChangeListener { rbRating, f1, b -> Rating.setText("Rating: ") }

        binding.btnpost.setOnClickListener {
            if (user.currentUser != null) {
                user.currentUser?.let {
                    //create post
                    CreatePost3(it.uid,imageList)
                }
            }
        }

        // uploading images from gallery
        binding.btnopengallery.setOnClickListener {
            if (!allPermissionsGranted()) {
                runtimePermissions
            } else {
                chooseImages()
            }
        }


        //Cancel button
        binding.btnCancelpost.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun chooseImages() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent,"Select Pictures"), PICK_IMAGE)
    }

    private fun CreatePost3(uid: String, imageList: ArrayList<Uri>){
        progressDialog?.show()
        if (imageList.size==0) {
            progressDialog?.dismiss()
            Toast.makeText(this, "Please Choose Images first", Toast.LENGTH_SHORT).show()
        }else{
            referance = FirebaseDatabase.getInstance().getReference("users")
            referance.child(uid).get().addOnSuccessListener {
                if (it.exists()) {
                    //get the current username from the real time database
                    val username = it.child("username").value.toString()
                    //call the CreatePost function and pass username from real time database
                    CreatePost2(uid, username,imageList)
                } else {

                    progressDialog?.dismiss()
                    Toast.makeText(this, "User don't exist", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {

                progressDialog?.dismiss()
                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //create post and save data to cloud database
    private fun CreatePost2(uid: String, username: String, imageList: ArrayList<Uri>) {

        urlString = ArrayList()


        val imageFolder: StorageReference =
            FirebaseStorage.getInstance().reference.child("ImagesFolder")

        upload_count = 0
        while (upload_count < imageList.size ){
            val individualImage: Uri = imageList[upload_count]
            val imageName: StorageReference =
                imageFolder.child("Images" + individualImage.lastPathSegment)
            imageName.putFile(individualImage).addOnSuccessListener(
                OnSuccessListener<Any?> {
                    imageName.downloadUrl.addOnSuccessListener(
                        OnSuccessListener<Uri> { uri ->
                            urlString.add(uri.toString())
                            if (urlString.size === imageList.size) {
                                createPost(uid,username,urlString)
                            }
                        }
                    )
                }
            )
            upload_count++
        }

    }

    private fun createPost(uid: String, username: String, urlString: ArrayList<String>) {

        val c: Date = Calendar.getInstance().time


        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val date: String = df.format(c)

        val title = binding.title.text.toString()
        val hastags = binding.hashtags.text.toString()
        val place = binding.place.text.toString()
        val rating = binding.rbRating.rating
        val description = binding.description.text.toString()



        var post = PostModel(uid, username,date,description,place, rating.toInt(),title,
            listOf(hastags),urlString)



        db.collection("Post").add(post).addOnSuccessListener {
            Toast.makeText(this, "Post Success", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            progressDialog?.dismiss()
        }.addOnFailureListener {
            Toast.makeText(this, "Post Failure.", Toast.LENGTH_SHORT).show()
            progressDialog?.dismiss()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (data.clipData != null) {
                        val countClipData = data.clipData!!.itemCount
                        var currentImageSlect = 0

                        while (currentImageSlect < countClipData) {

                            ImageUri = data.clipData!!.getItemAt(currentImageSlect).uri
                            ImageUri?.let { imageList.add(it) }
                            currentImageSlect += 1
                        }
                        binding.images.visibility = View.VISIBLE
                        binding.images.text = "You have selected " + imageList.size.toString() + " Images"
                    } else {
                        ImageUri = data.data
                        ImageUri?.let { imageList.add(it) }
                        binding.images.text = "You have selected Image"
                    }
                }else{
                    Toast.makeText(applicationContext,"You haven't select any image",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private val requiredPermissions: Array<String?>
        private get() = try {
            val info = this.packageManager
                .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            if (ps != null && ps.size > 0) {
                ps
            } else {
                arrayOfNulls(0)
            }
        } catch (e: Exception) {
            arrayOfNulls(0)
        }


    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (!isPermissionGranted(this, permission)) {
                return false
            }
        }
        return true
    }

    private val runtimePermissions: Unit
        private get() {
            val allNeededPermissions: MutableList<String?> = java.util.ArrayList()
            for (permission in requiredPermissions) {
                if (!isPermissionGranted(this, permission)) {
                    allNeededPermissions.add(permission)
                }
            }
            if (!allNeededPermissions.isEmpty()) {
                ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toTypedArray(), PERMISSION_REQUESTS
                )
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        Log.i("TAG", "Permission granted!")
        if (allPermissionsGranted()) {
            chooseImages()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    companion object {
        private const val PERMISSION_REQUESTS = 1

        fun isPermissionGranted(context: Context, permission: String?): Boolean {
            if (ContextCompat.checkSelfPermission(context, permission!!)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i("TAG", "Permission granted: $permission")
                return true
            }
            Log.i("TAG", "Permission NOT granted: $permission")
            return false
        }
    }
}
