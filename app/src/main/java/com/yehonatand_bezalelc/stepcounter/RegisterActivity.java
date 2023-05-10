package com.yehonatand_bezalelc.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    Button buttonLogin, buttonRegister, buttonForget;
    EditText editTextUsername, editTextPassword;
    DBPipeline dbPipeline;

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

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (dbPipeline.register(editTextUsername.getText().toString(), "mail", 0, 0f,
                        editTextPassword.getText().toString())) {
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}