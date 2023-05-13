package com.yehonatand_bezalelc.stepcounter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DBPipeline {

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    private FirebaseAuth mAuth;

    public DBPipeline(){
        mAuth = FirebaseAuth.getInstance();
    }

//    public void tryLogin(String email, String password, android.content.Context RegisterActivity){
//        if(TextUtils.isEmpty(email)){
//            Toast.makeText(RegisterActivity, "Enter email", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(TextUtils.isEmpty(password)){
//            Toast.makeText(RegisterActivity, "Enter password at least 6 characters long", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(RegisterActivity, "Account created.",
//                                    Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            if (password.length() >= 6){
//                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//
//                            }
//                            else {
//                                Toast.makeText(RegisterActivity.this, "Please enter password at least 6 characters long.",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//    }


    public boolean register(String username, String mail, int height, float weight, String password) {
        return true;
    }

    public boolean login(String username, String password) {
        return true;
    }
}
