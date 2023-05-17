package com.yehonatand_bezalelc.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashActivity extends AppCompatActivity {

    private boolean userConnected() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
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
//                startActivity(new Intent(SplashActivity.this, userConnected() ? HomeActivity.class : LoginActivity.class));
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}