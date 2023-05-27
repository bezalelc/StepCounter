package com.yehonatand_bezalelc.stepcounter;

import android.util.Patterns;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout textInputLayoutEmail, textInputLayoutHeight, textInputLayoutWeight, textInputLayoutPassword;
    FirebaseAuthHelper firebaseAuthHelper;

    FirebaseAuth mAuth;

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
        setContentView(R.layout.activity_register);

        textInputLayoutEmail = findViewById(R.id.register_text_input_layout_email);
        textInputLayoutHeight = findViewById(R.id.register_text_input_layout_height);
        textInputLayoutWeight = findViewById(R.id.register_text_input_layout_weight);
        textInputLayoutPassword = findViewById(R.id.register_text_input_layout_password);
        Button buttonRegister = findViewById(R.id.register_button_register);
        firebaseAuthHelper = new FirebaseAuthHelper();
        /*mAuth = FirebaseAuth.getInstance();*/
        mAuth = FirebaseAuth.getInstance();


        buttonRegister.setOnClickListener(v -> {
            String email = Objects.requireNonNull(textInputLayoutEmail.getEditText()).getText().toString().trim();
            String height = Objects.requireNonNull(textInputLayoutHeight.getEditText()).getText().toString().trim();
            String weight = Objects.requireNonNull(textInputLayoutWeight.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(textInputLayoutPassword.getEditText()).getText().toString().trim();

            boolean confirmEmail_ = confirmEmail(email), confirmHeight_ = confirmHeight(height),
                    confirmWeight_ = confirmWeight(weight), confirmPassword_ = confirmPassword(password);
            if (!(confirmEmail_ && confirmHeight_ && confirmWeight_ && confirmPassword_)) {
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Account created.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                            if (task.getException() instanceof FirebaseAuthException) {
                                FirebaseAuthException authException = (FirebaseAuthException) task.getException();
                                String errorCode = authException.getErrorCode();

                                // Handle specific error codes
                                switch (errorCode) {
                                    case "ERROR_EMAIL_ALREADY_IN_USE":
                                        textInputLayoutEmail.setError("Email already in use");
                                        break;
                                    case "ERROR_INVALID_EMAIL":
                                        textInputLayoutEmail.setError("Invalid email");
                                        break;
                                    case "ERROR_OPERATION_NOT_ALLOWED":
                                        textInputLayoutEmail.setError("Email/password accounts are not enabled");
                                        break;
                                    case "ERROR_WEAK_PASSWORD":
                                        textInputLayoutPassword.setError("Weak password");
                                        break;
                                    default:
                                        // Handle other error codes
                                        textInputLayoutPassword.setError(errorMessage);
                                        break;
                                }
                            } else {
                                // Handle other exceptions
                                textInputLayoutPassword.setError(errorMessage);
                            }
                        }
                    });

        });

        TextView textViewLogin = findViewById(R.id.register_button_have_account);
        textViewLogin.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
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

    private boolean confirmHeight(String height) {
        if (height.equals("")) {
            textInputLayoutHeight.setError("Enter your height");
            return false;
        } else if (Integer.parseInt(height) < 40) {
            textInputLayoutHeight.setError("Height must be > 40");
            return false;
        } else if (Integer.parseInt(height) > 230) {
            textInputLayoutHeight.setError("Height must be < 230");
            return false;
        }
        textInputLayoutHeight.setError("");
        return true;
    }

    private boolean confirmWeight(String weight) {
        if (weight.equals("")) {
            textInputLayoutWeight.setError("Enter your weight");
            return false;
        } else if (Integer.parseInt(weight) < 30) {
            textInputLayoutWeight.setError("Weight must be > 30");
            return false;
        } else if (Integer.parseInt(weight) > 300) {
            textInputLayoutWeight.setError("Weight must be < 300");
            return false;
        }
        textInputLayoutWeight.setError("");
        return true;
    }
}