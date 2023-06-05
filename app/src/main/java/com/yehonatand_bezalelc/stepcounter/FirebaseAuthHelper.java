package com.yehonatand_bezalelc.stepcounter;

import android.util.Patterns;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthHelper {

    public interface LoginRegisterCallback {
        void onSuccess(FirebaseUser user);

        void onFailure(Exception e);
    }

    public static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static FireBaseStatus login(String email, String password, final LoginRegisterCallback callback) {
        FireBaseStatus confirmEmailFireBaseStatus = confirmEmail(email), confirmPasswordFireBaseStatus = confirmPassword(password);
        if (confirmEmailFireBaseStatus != FireBaseStatus.FIELD_OK) {
            return confirmEmailFireBaseStatus;
        } else if (confirmPasswordFireBaseStatus != FireBaseStatus.FIELD_OK) {
            return confirmPasswordFireBaseStatus;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    callback.onSuccess(user);
                })
                .addOnFailureListener(callback::onFailure);

        return FireBaseStatus.FIELD_OK;
    }

    public static FireBaseStatus register(String email, String height, String weight, String password, final LoginRegisterCallback callback) {
        FireBaseStatus confirmHeightStatus = confirmHeight(height), confirmWeightStatus = confirmWeight(weight),
                confirmEmailStatus = confirmEmail(email), confirmPasswordStatus = confirmPassword(password);

        if (confirmEmailStatus != FireBaseStatus.FIELD_OK) {
            return confirmEmailStatus;
        } else if (confirmHeightStatus != FireBaseStatus.FIELD_OK) {
            return confirmHeightStatus;
        } else if (confirmWeightStatus != FireBaseStatus.FIELD_OK) {
            return confirmWeightStatus;
        } else if (confirmPasswordStatus != FireBaseStatus.FIELD_OK) {
            return confirmPasswordStatus;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    callback.onSuccess(user);
                })
                .addOnFailureListener(callback::onFailure);

        return FireBaseStatus.FIELD_OK;
    }

    private static FireBaseStatus confirmEmail(String email) {
        if (email.equals("")) {
            return FireBaseStatus.EMAIL_FIELD_EMPTY;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return FireBaseStatus.EMAIL_NOT_VALID;
        }
        return FireBaseStatus.FIELD_OK;
    }

    private static FireBaseStatus confirmPassword(String password) {
        if (password.equals("")) {
            return FireBaseStatus.PASSWORD_FIELD_EMPTY;
        } else if (password.length() < 6) {
            return FireBaseStatus.PASSWORD_LEN_LESS_THAN_6;
        }
        return FireBaseStatus.FIELD_OK;
    }

    private static FireBaseStatus confirmHeight(String height) {
        if (height.equals("")) {
            return FireBaseStatus.HEIGHT_FIELD_EMPTY;
        } else if (Integer.parseInt(height) < UserData.MIN_HEIGHT) {
            return FireBaseStatus.HEIGHT_TO_LOW;
        } else if (Integer.parseInt(height) > UserData.MAX_HEIGHT) {
            return FireBaseStatus.HEIGHT_TO_HIGH;
        }
        return FireBaseStatus.FIELD_OK;
    }

    private static FireBaseStatus confirmWeight(String weight) {
        if (weight.equals("")) {
            return FireBaseStatus.WEIGHT_FIELD_EMPTY;
        } else if (Integer.parseInt(weight) < UserData.MIN_WEIGHT) {
            return FireBaseStatus.WEIGHT_TO_LOW;
        } else if (Integer.parseInt(weight) > UserData.MAX_WEIGHT) {
            return FireBaseStatus.WEIGHT_TO_HIGH;
        }
        return FireBaseStatus.FIELD_OK;
    }

    public static boolean isUserConnected() {
        return firebaseAuth.getCurrentUser() != null;
    }
}
