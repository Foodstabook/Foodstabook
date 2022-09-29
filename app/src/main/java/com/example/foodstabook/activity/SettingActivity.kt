package com.example.foodstabook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.foodstabook.R
import com.example.foodstabook.databinding.ActivitySettingBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.delete_dialog.view.*

class SettingActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySettingBinding
    private lateinit var user: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        val actionbar = supportActionBar
        actionbar!!.title = "Settings"
        actionbar.setDisplayHomeAsUpEnabled(true)

        //show user email
        if (user.currentUser != null){
            user.currentUser?.let {
                binding.tvUserEmail.text = it.email
            }
        }

        binding.btnSignOut.setOnClickListener{
            user.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnDeactivate.setOnClickListener{

            btnDeactivate.setOnClickListener {
                //inflate dialog with custom view
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.delete_dialog,null)
                val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle("Deactivate Account")
                val mAlertDialog = mBuilder.show()
                mDialogView.btnDelete2.setOnClickListener {
                    mAlertDialog.dismiss()
                    val user = FirebaseAuth.getInstance().currentUser!!
                    user.delete()
                        .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,
                            "User deleted successfully",
                            Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                        }
                }
                mDialogView.btnCancel2.setOnClickListener {
                    mAlertDialog.dismiss()
                }

            }
        }
        goHome()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun goHome(){
        binding.logo.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}