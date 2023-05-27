package com.yehonatand_bezalelc.stepcounter;

import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ForgotPasswordActivity extends AppCompatActivity {
    boolean b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_paswword);

        Button buttonSubmit = findViewById(R.id.forgot_password_button_submit);
        buttonSubmit.setOnClickListener(view -> {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish();
        });
    }
}