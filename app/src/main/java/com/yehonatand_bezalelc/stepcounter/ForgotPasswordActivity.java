package com.yehonatand_bezalelc.stepcounter;

import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextInputLayout textInputLayoutEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_paswword);
        textInputLayoutEmail = findViewById(R.id.forgot_password_text_input_layout_email);
        Button buttonSubmit = findViewById(R.id.forgot_password_button_submit);


        buttonSubmit.setOnClickListener(v -> {
            String email = Objects.requireNonNull(textInputLayoutEmail.getEditText()).getText().toString().trim();
            FireBaseStatus fireBaseStatus = FirebaseAuthHelper.forgotPassword(email, new FirebaseAuthHelper.LoginRegisterCallback() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    Toast.makeText(ForgotPasswordActivity.this, "Reset Password Sent To Email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    // If sign in fails, display a message to the user.
                    if (e instanceof FirebaseAuthException) {
                        FirebaseAuthException authException = (FirebaseAuthException) e;
                        String errorCode = authException.getErrorCode();
                        FireBaseStatus errorCodeFireBaseStatus = FireBaseStatus.fromString(errorCode);
                        forgotPasswordErrorHandler(errorCodeFireBaseStatus, errorCode);
                    } else {
                        // Handle other exceptions
                        Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            forgotPasswordErrorHandler(fireBaseStatus, "");
        });
    }

    private void forgotPasswordErrorHandler(FireBaseStatus fireBaseStatus, String errorCode) {
        String emailFieldError = "", toastError = "";
        switch (fireBaseStatus) {
            case FIELD_OK:
                break;
            case SUCCESS:
                toastError = "Reset Password Sent To Email";
                break;
            case EMAIL_FIELD_EMPTY:
            case EMAIL_NOT_VALID:
            case ERROR_INVALID_EMAIL:
                emailFieldError = fireBaseStatus.getErrorMsg();
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
        if (!Objects.equals(toastError, "")) {
            Toast.makeText(ForgotPasswordActivity.this, toastError, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}