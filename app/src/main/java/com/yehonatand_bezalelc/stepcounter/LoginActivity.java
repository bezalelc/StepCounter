package com.yehonatand_bezalelc.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    Button buttonLogin, buttonRegister, buttonForget;
    EditText editTextUsername, editTextPassword;
    DBPipeline dbPipeline;

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

        dbPipeline = new DBPipeline();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (dbPipeline.login(editTextUsername.getText().toString(),
                        editTextPassword.getText().toString())) {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }
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