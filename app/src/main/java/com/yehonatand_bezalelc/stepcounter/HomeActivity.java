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

public class HomeActivity extends MainActivity implements ServiceConnection, StepCountObserver {
    private TextView textViewStepsTaken, textViewSteps;
    private StepCounterService.StepCounterBinder binder;
    private boolean bound = false;
    private static final int ACTIVITY_RECOGNITION_PERMISSION_CODE = 100;
    FirebaseAuth auth;
    FirebaseUser user;

    /*
    Main activity functions
     */
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

        checkActivityRecognitionPermission();

        textViewStepsTaken = findViewById(R.id.textViewStepsTaken);
        textViewSteps = findViewById(R.id.textViewSteps);
        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.stopButton);

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bound) {
            unbindService(this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void checkActivityRecognitionPermission() {
        // TODO handle 1. ACTIVITY_RECOGNITION not exist , 2. PERMISSION_DENIED
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)) {
            Toast.makeText(HomeActivity.this, "ACTIVITY_RECOGNITION not found in device", Toast.LENGTH_SHORT).show();
        }
        if (ContextCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            // Requesting ACTIVITY_RECOGNITION permission
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, ACTIVITY_RECOGNITION_PERMISSION_CODE);
        } else {
            Toast.makeText(HomeActivity.this, "ACTIVITY_RECOGNITION Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    public void startStepCounterService() {
        Intent stepCounterServiceIntent = new Intent(this, StepCounterService.class);
        startService(stepCounterServiceIntent);
        bindService(stepCounterServiceIntent, this, Context.BIND_AUTO_CREATE);
    }


    public void stopStepCounterService() {
        if (bound) {
            unbindService(this);
            bound = false;
        }

        Intent serviceIntent = new Intent(this, StepCounterService.class);
        stopService(serviceIntent);
    }

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
    public void onStepCountChanged(int stepCount) {
        updateStepCount(stepCount);
    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
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
            textViewStepsTaken.setText(Integer.toString(stepCount));
        } else {
            textViewStepsTaken.setText("#0");
        }
    }
}
