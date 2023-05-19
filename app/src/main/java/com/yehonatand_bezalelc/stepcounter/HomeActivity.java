package com.yehonatand_bezalelc.stepcounter;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends MainActivity implements ServiceConnection, StepCountObserver {
    private TextView textViewSteps, textViewGoal;
    private ImageButton buttonStartStop;
    private StepCounterService.StepCounterBinder binder;
    private boolean bound = false, count = true;
    private boolean isStepCounterSensorExistAndPermissionGranted = false;
    private static final int ACTIVITY_RECOGNITION_PERMISSION_CODE = 100;
    // TODO goal provided by user
    private static final int GOAL = 5000;
    FirebaseAuth auth;
    FirebaseUser user;

    /**
     * this function is for the MainActivity class, the MainActivity will start the correct UI
     * according to given id
     *
     * @return activity_home id
     */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home__;
    }

    /**
     * this function is for the MainActivity class, the MainActivity will start the correct nav bar
     * in the UI according to given id
     *
     * @return nav bar id id
     */
    @Override
    protected int getCurrentNavBarIdSelect() {
        return R.id.home;
    }

    ProgressBar progressBar;
//    ObjectAnimator progressBarObjectAnimator;

    @RequiresApi(api = Build.VERSION_CODES.Q)
//    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }


        checkActivityRecognitionPermission();

        textViewSteps = findViewById(R.id.daily_text_view_steps);
        textViewGoal = findViewById(R.id.daily_text_view_goal);
        buttonStartStop = findViewById(R.id.daily_button_start_stop);
        progressBar = findViewById(R.id.daily_progress_bar);
        progressBar.setMax(GOAL);

        if (count) {
            startStepCounterService();
            buttonStartStop.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            buttonStartStop.setImageResource(android.R.drawable.ic_media_play);
        }


        buttonStartStop.setOnClickListener(view -> {
            if (isStepCounterSensorExistAndPermissionGranted) {
                if (count) {
                    startStepCounterService();
                } else {
                    stopStepCounterService();
                }
                count = !count;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bound) {
            unbindService(this);
        }
    }

    /**
     * - check if step counter sensor exist in devise
     * - check if there is permission to use the sensor
     * - if sensor exist and permission denied the app will ask for permission
     * - if sensor not exist or permission denied, massage will raise
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void checkActivityRecognitionPermission() {
        // TODO handle 1. ACTIVITY_RECOGNITION not exist , 2. PERMISSION_DENIED
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)) {
//            Toast.makeText(HomeActivity.this, "ACTIVITY_RECOGNITION not found in device", Toast.LENGTH_SHORT).show();
            sensorProblemMakeAlert("Step Counter sensor not exist on this device and the application will not work properly");
        } else {
            if (ContextCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
                // Requesting ACTIVITY_RECOGNITION permission
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, ACTIVITY_RECOGNITION_PERMISSION_CODE);
            } else {
                isStepCounterSensorExistAndPermissionGranted = true;
//                Toast.makeText(HomeActivity.this, "ACTIVITY_RECOGNITION Permission already granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ACTIVITY_RECOGNITION_PERMISSION_CODE) {
            isStepCounterSensorExistAndPermissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (!isStepCounterSensorExistAndPermissionGranted) {
                sensorProblemMakeAlert("Without physical activity permission the application will not work properly");
            }
        }
    }

    public void sensorProblemMakeAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> {
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void startStepCounterService() {
        Intent stepCounterServiceIntent = new Intent(this, StepCounterService.class);
        startService(stepCounterServiceIntent);
        bindService(stepCounterServiceIntent, this, Context.BIND_AUTO_CREATE);
        buttonStartStop.setImageResource(android.R.drawable.ic_media_pause);
    }

    public void stopStepCounterService() {
        if (bound) {
            Toast.makeText(this, "stopStepCounterService: if (bound)", Toast.LENGTH_LONG).show();
            unbindService(this);
            bound = false;
        }

        Intent serviceIntent = new Intent(this, StepCounterService.class);
        stopService(serviceIntent);
        buttonStartStop.setImageResource(android.R.drawable.ic_media_play);
    }

    @Override
    public void onStepCountChanged(int stepCount) {
        updateStepCount(stepCount);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        Toast.makeText(this, "onServiceConnected", Toast.LENGTH_LONG).show();
        this.binder = (StepCounterService.StepCounterBinder) service;
        bound = true;
        binder.getService().addObserver(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        bound = false;

        if (binder != null) {
            binder.getService().removeObserver(this);
        }
    }

    @SuppressLint("SetTextI18n")
    public void updateStepCount(int stepCount) {
        if (bound) {
            textViewSteps.setText(Integer.toString(stepCount));
            progressBar.setProgress(Math.min(stepCount, GOAL));
        } else {
            textViewSteps.setText("#0");
        }
    }
}
