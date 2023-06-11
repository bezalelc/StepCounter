package com.yehonatand_bezalelc.stepcounter;

import android.util.Patterns;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Objects;


public class FirebaseAuthHelper {

    public interface LoginRegisterCallback {
        void onSuccess(FirebaseUser user);

        void onFailure(Exception e);
    }

    public static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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

        Date thisDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String nowDate = dateFormat.format(thisDate);
        Map<String, Object> userInfo = new HashMap<String, Object>();
        UserData userData = UserData.getInstance();
        userData.resetValues();

        userData.setEmail(email);
        userData.setHeight(Integer.parseInt(height));
        userData.setWeight(Integer.parseInt(weight));

        userInfo.put("height", Integer.parseInt(height));
        userInfo.put("weight", Integer.parseInt(weight));
        userInfo.put("goal", userData.getGoal());
        userInfo.put("step_counter", userData.getStepsCounter());
        userInfo.put("steps_counter_last", userData.getStepsCounterLast());
        userInfo.put("battery_threshold", userData.getSaveBatteryThreshold());



        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
//                    addCollection(email,"General", userInfo);
                    firebaseFirestore.collection(email).document("General")
                            .set(userInfo, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    callback.onSuccess(null);
                                }
                            })
                            .addOnFailureListener(callback::onFailure);
                })
                .addOnFailureListener(callback::onFailure);

        return FireBaseStatus.FIELD_OK;
    }


//    public static FireBaseStatus addCollection(String email, String docName, Map inputData, final LoginRegisterCallback callback) {
//        FireBaseStatus confirmEmailStatus = confirmEmail(email);
//
//        if (confirmEmailStatus != FireBaseStatus.FIELD_OK) {
//            return confirmEmailStatus;
//        }
//
//        Date thisDate = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        Map<String, Object> userInfo = new HashMap<String, Object>();
//        userInfo.put("height", height);
//        userInfo.put("weight", weight);
//
//
//
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnSuccessListener(authResult -> {
//                    addCollection(email, dateFormat.format(thisDate), userInfo, callback);
//                })
//                .addOnFailureListener(callback::onFailure);
//
//        return FireBaseStatus.FIELD_OK;
//    }

    public static FireBaseStatus deleteCollection(String email, String docName){
/// TODO: 09/06/2023  fix this
        firebaseFirestore.collection(email).document(docName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        return FireBaseStatus.FIELD_OK;
    }

    public static FireBaseStatus updateCollection(String email, String docName, String field_name, Object inputData){
        DocumentReference collectionRef = firebaseFirestore.collection(email).document(docName);


        collectionRef.update(field_name, inputData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error updating document", e);
                    }
                });
        return FireBaseStatus.FIELD_OK;
    }

    public static FireBaseStatus loadUser(String email, String docName){
        UserData userInstance = UserData.getInstance();
        DocumentReference collectionRef = firebaseFirestore.collection(email).document(docName);
        collectionRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Retrieve data from the document snapshot
//                  TODO make one function for hem all in userdata
                    userInstance.setGoal(Objects.requireNonNull(documentSnapshot.getLong("goal")).intValue());
                    userInstance.setHeight(Objects.requireNonNull(documentSnapshot.getLong("height")).intValue());
                    userInstance.setWeight(Objects.requireNonNull(documentSnapshot.getLong("weight")).intValue());
                    userInstance.setStepsCounter(Objects.requireNonNull(documentSnapshot.getLong("step_counter")).intValue());
                    userInstance.setStepsCounterLast(Objects.requireNonNull(documentSnapshot.getLong("steps_counter_last")).intValue());
                    userInstance.setSaveBatteryThreshold(Objects.requireNonNull(documentSnapshot.getLong("battery_threshold")).intValue());

                    // Do something with the data
                    // ...
                } else {
                    // Document doesn't exist
                }
            }
        });
        return FireBaseStatus.FIELD_OK;
    }

    public static FireBaseStatus addCollection(String email, String docName, Map inputData){
/// TODO: 09/06/2023  fix this
        firebaseFirestore.collection(email).document(docName)
                .set(inputData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        lis
//                                res =  true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//
//                        res =  false;
                    }
                });
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

    public static FireBaseStatus forgotPassword(String email, final LoginRegisterCallback callback) {
        FireBaseStatus confirmEmailStatus = confirmEmail(email);

        if (confirmEmailStatus != FireBaseStatus.FIELD_OK) {
            return confirmEmailStatus;
        }

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(authResult -> {
                    callback.onSuccess(null);
                })
                .addOnFailureListener(callback::onFailure);

        return FireBaseStatus.FIELD_OK;
    }


    public static boolean isUserConnected() {
        return firebaseAuth.getCurrentUser() != null;
    }
}
