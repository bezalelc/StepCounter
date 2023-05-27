package com.yehonatand_bezalelc.stepcounter;

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

        buttonLogin.setOnClickListener(v -> {
            String email = Objects.requireNonNull(textInputLayoutEmail.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(textInputLayoutPassword.getEditText()).getText().toString().trim();
            FireBaseStatus fireBaseStatus =
                    FirebaseAuthHelper.login(email, password, new FirebaseAuthHelper.LoginRegisterCallback() {
                        @Override
                        public void onSuccess(FirebaseUser user) {
                            Toast.makeText(LoginActivity.this, "Sign in successful.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            // If sign in fails, display a message to the user.
                            if (e instanceof FirebaseAuthException) {
                                FirebaseAuthException authException = (FirebaseAuthException) e;
                                String errorCode = authException.getErrorCode();
                                FireBaseStatus errorCodeFireBaseStatus = FireBaseStatus.fromString(errorCode);
                                loginErrorHandler(errorCodeFireBaseStatus, errorCode);
                            } else {
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            loginErrorHandler(fireBaseStatus, "");
        });

        textViewForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            finish();
        });
        textViewRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }

    private void loginErrorHandler(FireBaseStatus fireBaseStatus, String errorCode) {
        String emailFieldError = "", passwordFieldError = "", toastError = "";
        switch (fireBaseStatus) {
            case FIELD_OK:
                break;
            case SUCCESS:
                toastError = "Login success";
                break;
            case EMAIL_FIELD_EMPTY:
            case EMAIL_NOT_VALID:
            case ERROR_INVALID_EMAIL:
            case ERROR_USER_NOT_FOUND:
            case ERROR_USER_DISABLED:
                emailFieldError = fireBaseStatus.getErrorMsg();
                break;
            case PASSWORD_FIELD_EMPTY:
            case PASSWORD_LEN_LESS_THAN_6:
            case ERROR_WRONG_PASSWORD:
                passwordFieldError = fireBaseStatus.getErrorMsg();
                break;
            case ERROR_TOO_MANY_REQUESTS:
            case ERROR_OPERATION_NOT_ALLOWED:
                toastError = fireBaseStatus.getErrorMsg();
                break;
            case ERROR_ELSE:
            default:
                toastError = errorCode;
                break;
        }

        textInputLayoutEmail.setError(emailFieldError);
        textInputLayoutPassword.setError(passwordFieldError);
        if (!Objects.equals(toastError, "")) {
            Toast.makeText(LoginActivity.this, toastError, Toast.LENGTH_SHORT).show();
        }
    }
}