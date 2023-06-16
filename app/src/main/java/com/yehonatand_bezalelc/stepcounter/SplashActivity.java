package com.yehonatand_bezalelc.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.google.firebase.auth.FirebaseUser;

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
                FirebaseUser currentUser = FirebaseAuthHelper.firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    UserData.getInstance().setEmail(currentUser.getEmail());
                    UserData.getInstance().isTodayExist();
                    FirebaseAuthHelper.loadUser(currentUser.getEmail(), UserData.GENERAL, new FirebaseAuthHelper.LoadUserDataCallback() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 300);
    }
}