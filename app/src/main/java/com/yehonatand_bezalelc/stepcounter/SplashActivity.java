package com.yehonatand_bezalelc.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private boolean userConnected() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent;
////                Class<? extends AppCompatActivity> nextActivity = userConnected() ? MainActivity.class : LoginActivity.class;
//                if (userConnected())
//                    intent = new Intent(SplashActivity.this, LoginActivity.class);
//                else
//                    intent = new Intent(SplashActivity.this, MainActivity.class);
////                startActivity(new Intent(SplashActivity.this, nextActivity));
                startActivity(new Intent(SplashActivity.this, userConnected() ? MainActivity.class : LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}