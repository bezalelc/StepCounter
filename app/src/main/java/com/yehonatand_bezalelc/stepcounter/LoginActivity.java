package com.yehonatand_bezalelc.stepcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_login);

        buttonLogin = findViewById(R.id.buttonLogin);
        editTextUsername = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonForget = findViewById(R.id.buttonForget);
        buttonRegister = findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();


        dbPipeline = new DBPipeline();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                if (dbPipeline.login(editTextUsername.getText().toString(),
//                        editTextPassword.getText().toString())) {
//                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                    finish();
//                }

                String email, password;
                email = String.valueOf(editTextUsername.getText());
                password = String.valueOf(editTextPassword.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Please enter username in email format\n" +
                                    "Enter password at least 6 characters long.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password) || password.length() < 6){
                    Toast.makeText(LoginActivity.this, "Enter password at least 6 characters long.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login Successful.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(LoginActivity.this, "Login failed.",
                                            Toast.LENGTH_SHORT).show();
                                    Toast.makeText(LoginActivity.this, "Please enter username in email format\nEnter password at least 6 characters long.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }
}