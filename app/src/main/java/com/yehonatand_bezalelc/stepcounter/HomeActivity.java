package com.yehonatand_bezalelc.stepcounter;

import android.annotation.SuppressLint;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends MainActivity implements ServiceConnection, StepCountObserver {
    private TextView textViewSteps;
    private TextView textViewProgressBar;
    private ImageButton buttonStartStop;
    private StepCounterService.StepCounterBinder binder = null;
    // TODO count defined by user
    private boolean count = false;
    private boolean isStepCounterSensorExistAndPermissionGranted = false;
    private static final int ACTIVITY_RECOGNITION_PERMISSION_CODE = 100;
    //    private static final int PROGRESS_BAR_MAX = 1000;
    // TODO goal provided by user
    private static final int GOAL = 30000, STEPS = 4000;
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
        return R.layout.activity_home;
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

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.Q)
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
        TextView textViewGoal = findViewById(R.id.daily_text_view_goal);
        textViewGoal.setText("/" + GOAL);
        textViewProgressBar = findViewById(R.id.daily_text_view_progress_bar);
        buttonStartStop = findViewById(R.id.daily_button_start_stop);
        progressBar = findViewById(R.id.daily_progress_bar);
        progressBar.setMax(GOAL);

        if (count && isStepCounterSensorExistAndPermissionGranted) {
            startStepCounterService();
        } else {
            stopStepCounterService();
        }

        // Add a layout change listener to the ProgressBar
        progressBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updateStepCount(binder != null ? binder.getStepCount() : STEPS);
                // Remove the listener to avoid redundant calls
                progressBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        buttonStartStop.setOnClickListener(view -> {
            count = !count;
            if (isStepCounterSensorExistAndPermissionGranted) {
                if (binder != null) {
                    stopStepCounterService();
                } else {
                    startStepCounterService();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binder != null) {
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
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)) {
            sensorProblemMakeAlert("Step Counter sensor not exist on this device and the application will not work properly");
        } else {
            if (ContextCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
                // Requesting ACTIVITY_RECOGNITION permission
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, ACTIVITY_RECOGNITION_PERMISSION_CODE);
            } else {
                isStepCounterSensorExistAndPermissionGranted = true;
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
        if (BatteryReceiverHandler.checkBatteryLevel(this, StepCounterService.BATTERY_LEVEL_THRESHOLD)) {
            createBatteryAlert();
            return;
        }
        Intent stepCounterServiceIntent = new Intent(this, StepCounterService.class);
        startService(stepCounterServiceIntent);
        bindService(stepCounterServiceIntent, this, Context.BIND_AUTO_CREATE);
        buttonStartStop.setImageResource(android.R.drawable.ic_media_pause);
    }

    public void createBatteryAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your battery is too low. Steps counting is not available.\nplease charge your phone if you want to start step counting or low your battery saver")
                .setCancelable(false)
                .setPositiveButton("Yes", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void stopStepCounterService() {
        if (binder != null) {
            binder.removeRunningNotification();
            unbindService(this);
            binder = null;
        }
        count = false;

        stopService(new Intent(this, StepCounterService.class));
        buttonStartStop.setImageResource(android.R.drawable.ic_media_play);
    }

    @Override
    public void onStepCountChanged(int stepCount) {
        updateStepCount(stepCount);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        this.binder = (StepCounterService.StepCounterBinder) service;
        binder.getService().addObserver(this);
//        Toast.makeText(this, "onServiceConnected: (binder == null)=" + (binder == null) + ", bound=" + bound + ", count=" + count, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (binder != null) {
            binder.getService().removeObserver(this);
        }
//        Toast.makeText(this, "onServiceDisconnected: (binder == null)=" + (binder == null) + ", bound=" + bound + ", count=" + count, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    public void updateStepCount(int stepCount) {
        textViewSteps.setText(Integer.toString(stepCount));

        int maxProgress = progressBar.getMax();
        stepCount = Math.min(stepCount, maxProgress);
        float progressPercentage = (float) stepCount / maxProgress;
        int progressBarLocation = (int) (progressPercentage * progressBar.getWidth());

        textViewProgressBar.setText(((int) (100 * progressPercentage)) + "%");
        if (progressPercentage < .7) {
            textViewProgressBar.setPadding(progressBarLocation + 5, 0, 0, 0);
        } else {
            String text = textViewProgressBar.getText().toString();
            Paint paint = textViewProgressBar.getPaint();
            int textLengthInPixels = (int) paint.measureText(text);
            textViewProgressBar.setPadding(progressBarLocation - textLengthInPixels - 20, 0, 0, 0);
        }
        progressBar.setProgress(stepCount);
    }
}
