package com.yehonatand_bezalelc.stepcounter;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout textInputLayoutEmail, textInputLayoutHeight, textInputLayoutWeight, textInputLayoutPassword;

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

        buttonRegister.setOnClickListener(v -> {
            String email = Objects.requireNonNull(textInputLayoutEmail.getEditText()).getText().toString().trim();
            String height = Objects.requireNonNull(textInputLayoutHeight.getEditText()).getText().toString().trim();
            String weight = Objects.requireNonNull(textInputLayoutWeight.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(textInputLayoutPassword.getEditText()).getText().toString().trim();
            FireBaseStatus fireBaseStatus = FirebaseAuthHelper.register(email, height, weight, password, new FirebaseAuthHelper.LoginRegisterCallback() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    Toast.makeText(RegisterActivity.this, "Account created.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
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
                        registerErrorHandler(errorCodeFireBaseStatus, errorCode);
                    } else {
                        // Handle other exceptions
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            registerErrorHandler(fireBaseStatus, "");
        });

        TextView textViewLogin = findViewById(R.id.register_button_have_account);

        textViewLogin.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerErrorHandler(FireBaseStatus fireBaseStatus, String errorCode) {
        String emailFieldError = "", heightFieldError = "", weightFieldError = "", passwordFieldError = "", toastError = "";
        switch (fireBaseStatus) {
            case FIELD_OK:
                break;
            case SUCCESS:
                toastError = "Register success";
                break;
            case EMAIL_FIELD_EMPTY:
            case EMAIL_NOT_VALID:
            case ERROR_INVALID_EMAIL:
            case ERROR_EMAIL_ALREADY_IN_USE:
                emailFieldError = fireBaseStatus.getErrorMsg();
                break;
            case PASSWORD_FIELD_EMPTY:
            case PASSWORD_LEN_LESS_THAN_6:
            case ERROR_WEAK_PASSWORD:
                passwordFieldError = fireBaseStatus.getErrorMsg();
                break;
            case HEIGHT_FIELD_EMPTY:
            case HEIGHT_TO_HIGH:
            case HEIGHT_TO_LOW:
                heightFieldError = fireBaseStatus.getErrorMsg();
                break;
            case WEIGHT_FIELD_EMPTY:
            case WEIGHT_TO_HIGH:
            case WEIGHT_TO_LOW:
                weightFieldError = fireBaseStatus.getErrorMsg();
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
        textInputLayoutHeight.setError(heightFieldError);
        textInputLayoutWeight.setError(weightFieldError);
        textInputLayoutPassword.setError(passwordFieldError);
        if (!Objects.equals(toastError, "")) {
            Toast.makeText(RegisterActivity.this, toastError, Toast.LENGTH_SHORT).show();
        }
    }

}