package com.yehonatand_bezalelc.stepcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    Button buttonLogin, buttonRegister, buttonForget;
    EditText editTextUsername, editTextPassword;
    DBPipeline dbPipeline;

    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonRegister = findViewById(R.id.buttonRegister);
        editTextUsername = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        dbPipeline = new DBPipeline();
        /*mAuth = FirebaseAuth.getInstance();*/
        mAuth = dbPipeline.getmAuth();


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                if (dbPipeline.register(editTextUsername.getText().toString(), "mail", 0, 0f,
//                        editTextPassword.getText().toString())) {
//                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
//                    finish();
//                }

                String email, password;
                email = String.valueOf(editTextUsername.getText());
                password = String.valueOf(editTextPassword.getText());



                if(TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Enter password at least 6 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    if (password.length() >= 6){
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this, "Please enter password at least 6 characters long.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}