package com.yehonatand_bezalelc.stepcounter;

import android.annotation.SuppressLint;
import android.util.Patterns;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;


public class FirebaseAuthHelper {

    public interface LoginRegisterCallback {
        void onSuccess(FirebaseUser user);

        void onFailure(Exception e);
    }

    public interface LoadUserDataCallback {
        void onSuccess();
    }

    public static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @SuppressLint("StaticFieldLeak")
    public static final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

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

        Map<String, Object> userInfo = new HashMap<>();
        UserData userData = UserData.getInstance();
        userData.resetValues();

        userData.setEmail(email);
        userData.setHeight(Integer.parseInt(height));
        userData.setWeight(Integer.parseInt(weight));

        userInfo.put("height", Integer.parseInt(height));
        userInfo.put("weight", Integer.parseInt(weight));
        userInfo.put("goal", userData.getGoal());
        userInfo.put("steps_counter_last", userData.getStepsCounterLast());
        userInfo.put("battery_threshold", userData.getSaveBatteryThreshold());

        Map<String, Integer> week_history;
        week_history = userData.getHistory();
        userInfo.put("history", week_history);
        userData.isTodayExist();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> firebaseFirestore.collection(email).document("General")
                        .set(userInfo, SetOptions.merge())
                        .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                        .addOnFailureListener(callback::onFailure))
                .addOnFailureListener(callback::onFailure);

        return FireBaseStatus.FIELD_OK;
    }

    public static void updateCollection(String email, String docName, String field_name, Object inputData){
        DocumentReference collectionRef = firebaseFirestore.collection(email).document(docName);


        collectionRef.update(field_name, inputData)
                .addOnSuccessListener(aVoid -> {});
    }

    public static void loadUser(String email, String docName, final LoadUserDataCallback callback){
        UserData userInstance = UserData.getInstance();
        DocumentReference collectionRef = firebaseFirestore.collection(email).document(docName);
        collectionRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve data from the document snapshot
                int input_goal = Objects.requireNonNull(documentSnapshot.getLong("goal")).intValue();
                int input_weight = Objects.requireNonNull(documentSnapshot.getLong("weight")).intValue();
                int input_height = Objects.requireNonNull(documentSnapshot.getLong("height")).intValue();
                int input_threshold_battery = Objects.requireNonNull(documentSnapshot.getLong("battery_threshold")).intValue();
                HashMap<String, Integer> input_HM = (HashMap<String, Integer>) documentSnapshot.get("history");
                userInstance.initUserDate(input_goal, input_weight, input_height, input_threshold_battery, input_HM);
                callback.onSuccess();
            }
        });
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

    public static FireBaseStatus forgotPassword(String email, final LoginRegisterCallback callback) {
        FireBaseStatus confirmEmailStatus = confirmEmail(email);

        if (confirmEmailStatus != FireBaseStatus.FIELD_OK) {
            return confirmEmailStatus;
        }

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(authResult -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);

        return FireBaseStatus.FIELD_OK;
    }

    public static boolean isUserNotConnected() {
        return firebaseAuth.getCurrentUser() == null;
    }
}
