package com.example.foodstabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView banner, registerUser;
    private EditText editTextusername, editTextage, editTextemail, editTextpassword;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();
        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextusername = (EditText) findViewById(R.id.username);
        editTextage = (EditText) findViewById(R.id.age);
        editTextemail = (EditText) findViewById(R.id.email);
        editTextpassword = (EditText) findViewById(R.id.password);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerUser:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        String email = editTextemail.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();
        String username = editTextusername.getText().toString().trim();
        String age = editTextage.getText().toString().trim();

        if(username.isEmpty()) {
            editTextusername.setError("username is required");
            editTextusername.requestFocus();
            return;
        }
        if(age.isEmpty()) {
            editTextage.setError("age is required");
            editTextage.requestFocus();
            return;
        }
        if(email.isEmpty()) {
            editTextemail.setError("email is required");
            editTextemail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextpassword.setError("password is required");
            editTextpassword.requestFocus();
            return;
        }
//        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            editTextemail.setError("Valid email required");
//            editTextemail.requestFocus();
//            return;
//        }

        if(password.length() < 8) {
            editTextpassword.setError("Password must be 8 or more characters in length");
            editTextpassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            User user = new User(username, age, email);

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()) {
                                                Toast.makeText(RegisterUserActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();

                                            }else Toast.makeText(RegisterUserActivity.this, "Failed to register user1", Toast.LENGTH_LONG).show();
                                        }
                                    });


                        }else{
                            Toast.makeText(RegisterUserActivity.this, "Failed to register user2", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}