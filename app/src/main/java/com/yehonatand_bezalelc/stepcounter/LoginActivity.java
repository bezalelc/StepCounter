package com.yehonatand_bezalelc.stepcounter;

import android.util.Patterns;
import android.widget.TextView;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
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

        Button buttonLogin = findViewById(R.id.login_button_login);
        textInputLayoutEmail = findViewById(R.id.login_text_input_layout_email);
        textInputLayoutPassword = findViewById(R.id.login_text_input_layout_password);
        TextView textViewRegister = findViewById(R.id.login_button_dont_have_account);
        TextView textViewForgotPassword = findViewById(R.id.login_text_view_forgot_password);


        mAuth = FirebaseAuth.getInstance();


        DBPipeline dbPipeline = new DBPipeline();

        buttonLogin.setOnClickListener(v -> {
            String email = Objects.requireNonNull(textInputLayoutEmail.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(textInputLayoutPassword.getEditText()).getText().toString().trim();

            boolean confirmEmail_ = confirmEmail(email), confirmPassword_ = confirmPassword(password);
            if (!(confirmEmail_ && confirmPassword_)) {
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // User signed in successfully
                            Toast.makeText(LoginActivity.this, "Sign in successful.",
                                    Toast.LENGTH_SHORT).show();
                            // Proceed with your desired actions
                        } else {
                            // If sign in fails, display a message to the user.
                            String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                            if (task.getException() instanceof FirebaseAuthException) {
                                FirebaseAuthException authException = (FirebaseAuthException) task.getException();
                                String errorCode = authException.getErrorCode();

                                // Handle specific error codes
                                switch (errorCode) {
                                    case "ERROR_INVALID_EMAIL":
                                        textInputLayoutEmail.setError("Invalid email");
                                        break;
                                    case "ERROR_WRONG_PASSWORD":
                                        textInputLayoutPassword.setError("Wrong password");
                                        break;
                                    case "ERROR_USER_NOT_FOUND":
                                        textInputLayoutEmail.setError("User not found");
                                        break;
                                    case "ERROR_USER_DISABLED":
                                        textInputLayoutEmail.setError("User account disabled");
                                        break;
                                    case "ERROR_TOO_MANY_REQUESTS":
                                        Toast.makeText(LoginActivity.this, "Too many unsuccessful attempts. Please try again later.",
                                                Toast.LENGTH_SHORT).show();
                                        break;
                                    case "ERROR_OPERATION_NOT_ALLOWED":
                                        Toast.makeText(LoginActivity.this, "Email/password authentication is not enabled.",
                                                Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        // Handle other error codes
                                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            } else {
                                // Handle other exceptions
                                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        });
        textViewForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPaswwordActivity.class));
            finish();
        });
        textViewRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }

    private boolean confirmEmail(String email) {
        if (email.equals("")) {
            textInputLayoutEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputLayoutEmail.setError("Please enter a valid email address");
            return false;
        }
        textInputLayoutEmail.setError("");
        return true;
    }

    private boolean confirmPassword(String password) {
        if (password.equals("")) {
            textInputLayoutPassword.setError("Field can't be empty");
            return false;
        } else if (password.length() < 6) {
            textInputLayoutPassword.setError("Please enter password at least 6 characters long");
            return false;
        }
        textInputLayoutPassword.setError("");
        return true;
    }
}