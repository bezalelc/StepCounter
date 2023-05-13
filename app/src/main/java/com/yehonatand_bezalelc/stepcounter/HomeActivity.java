package com.yehonatand_bezalelc.stepcounter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.common.subtyping.qual.Bottom;


public class HomeActivity extends MainActivity {
    private int steps = 0;
    private SensorManager sensorManager;
    private Sensor sensorStepCounter;
    private boolean isStepCounterSensorRunning = false;
    private TextView textViewStepsTaken;
    private Button startButton, stopButton;

    FirebaseAuth auth;
    FirebaseUser user;
    private static final int ACTIVITY_RECOGNITION_PERMISSION_CODE = 100;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    protected int getCurrentNavBarIdSelect() {
        return R.id.home;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textViewStepsTaken = findViewById(R.id.textViewStepsTaken);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startStepCounterService();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopStepCounterService();
            }
        });

        PackageManager packageManager = getPackageManager();
//        TODO check if sensor exist
//        boolean b = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER);

        checkActivityRecognitionPermission();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
//            sensorStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//            isStepCounterSensorRunning = true;
//            Toast.makeText(this, "true" + steps, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "false" + steps, Toast.LENGTH_SHORT).show();
//            isStepCounterSensorRunning = false;
//        }
    }

    public void startStepCounterService() {
        if (!StepCounterService.isRunning()) {
            Intent serviceIntent = new Intent(this, StepCounterService.class);
            startService(serviceIntent);
        }
    }

    public void stopStepCounterService() {
        if (StepCounterService.isRunning()) {
            Intent serviceIntent = new Intent(this, StepCounterService.class);
            stopService(serviceIntent);
        }
    }

    // Function to check and request permission.
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void checkActivityRecognitionPermission() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, ACTIVITY_RECOGNITION_PERMISSION_CODE);
        } else {
            Toast.makeText(HomeActivity.this, "ACTIVITY_RECOGNITION Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ACTIVITY_RECOGNITION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(HomeActivity.this, "ACTIVITY_RECOGNITION Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HomeActivity.this, "ACTIVITY_RECOGNITION Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        isStepCounterSensorRunning = true;
//        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        if (sensor != null) {
//            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
//        } else {
//            Toast.makeText(this, "false " + steps, Toast.LENGTH_SHORT).show();
//        }
//        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
//            sensorManager.registerListener(this, sensorStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStepCounterSensorRunning = false;
//        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
//            sensorManager.unregisterListener(this, sensorStepCounter);
//        }
    }


//    @SuppressLint("SetTextI18n")
//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        if (isStepCounterSensorRunning) {
////            Toast.makeText(this, "steps " + sensorEvent.values[0], Toast.LENGTH_SHORT).show();
//            textViewStepsTaken.setText(Integer.toString((int) sensorEvent.values[0]));
//        }
//
////        if (sensorEvent.sensor == sensorStepCounter) {
////            steps = (int) sensorEvent.values[0];
////            Toast.makeText(this, "steps " + steps, Toast.LENGTH_SHORT).show();
////        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }


}
