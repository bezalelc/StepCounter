package com.yehonatand_bezalelc.stepcounter;

import android.annotation.SuppressLint;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.*;

public class HomeActivity extends MainActivity implements ServiceConnection, StepCountObserver {
    private TextView textViewSteps;
    private TextView textViewProgressBar;
    private ImageButton buttonStartStop;
    private ProgressBar progressBar;
    private StepCounterService.StepCounterBinder binder = null;
    private boolean isStepCounterSensorExistAndPermissionGranted = false;
    private static final int ACTIVITY_RECOGNITION_PERMISSION_CODE = 100;
    //    private static final int PROGRESS_BAR_MAX = 1000;
    // TODO goal provided by user
    private final static UserData userData = UserData.getInstance();

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


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!FirebaseAuthHelper.isUserConnected()) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        checkActivityRecognitionPermission();

        setStatisticViews();

        textViewSteps = findViewById(R.id.daily_text_view_steps);
        TextView textViewGoal = findViewById(R.id.daily_text_view_goal);
        textViewGoal.setText("/" + userData.getGoal());
        textViewProgressBar = findViewById(R.id.daily_text_view_progress_bar);
        buttonStartStop = findViewById(R.id.daily_button_start_stop);
        progressBar = findViewById(R.id.daily_progress_bar);
        progressBar.setMax(userData.getGoal());

        if (userData.isCount() && isStepCounterSensorExistAndPermissionGranted) {
            startStepCounterService();
        } else {
            stopStepCounterService();
        }

        // Add a layout change listener to the ProgressBar
        progressBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updateStepCount();
                // Remove the listener to avoid redundant calls
                progressBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        buttonStartStop.setOnClickListener(view -> {
            if (isStepCounterSensorExistAndPermissionGranted) {
                userData.setCount(!userData.isCount());

                if (binder != null) {
                    stopStepCounterService();
                } else {
                    startStepCounterService();
                }
            }
        });

    }

//    @SuppressLint("SetTextI18n")
//    public void setTodayStatisticView() {
//        TextView textViewDistance = findViewById(R.id.daily_text_view_km_num);
//        textViewDistance.setText(Double.toString((double) userData.stepsToDistance(userData.getStepsCounter()) / 100.0));
//        TextView textViewCalories = findViewById(R.id.daily_text_view_calories_num);
//        textViewCalories.setText(Integer.toString(userData.calculateCaloriesBurned(userData.getStepsCounter())));
//        TextView textViewAverage = findViewById(R.id.daily_text_view_average);
//        Integer[] summerySorted = userData.getSummerySorted();
//        int average = 0;
//        for (Integer integer : summerySorted) {
//            average += integer;
//        }
//        average /= summerySorted.length;
//        textViewAverage.setText(Integer.toString(average));
//
//        ImageView icon = findViewById(R.id.daily_image_view_summery_icon7);
//        if (summerySorted[6] >= userData.getGoal()) {
//            icon.setImageResource(R.drawable.baseline_logout_24);
//        } else {
//            icon.setImageResource(R.drawable.back_btn);
//        }
//    }

    @SuppressLint("SetTextI18n")
    private void setStatisticViews() {
        TextView textViewDistance = findViewById(R.id.daily_text_view_km_num);
        textViewDistance.setText(Double.toString((double) userData.stepsToDistance(userData.getStepsCounter()) / 100.0));
        TextView textViewCalories = findViewById(R.id.daily_text_view_calories_num);
        textViewCalories.setText(Integer.toString(userData.calculateCaloriesBurned(userData.getStepsCounter())));
        TextView textViewAverage = findViewById(R.id.daily_text_view_average);
        Integer[] summerySorted = userData.getSummerySorted();
        Log.d("setStatisticViews", Arrays.toString(summerySorted));
        int average = 0;
        for (Integer integer : summerySorted) {
            average += integer;
        }
        average /= summerySorted.length;
        textViewAverage.setText(Integer.toString(average));
//        setTodayStatisticView();

        ImageView[] icons = new ImageView[7];
        icons[0] = findViewById(R.id.daily_image_view_summery_icon1);
        icons[1] = findViewById(R.id.daily_image_view_summery_icon2);
        icons[2] = findViewById(R.id.daily_image_view_summery_icon3);
        icons[3] = findViewById(R.id.daily_image_view_summery_icon4);
        icons[4] = findViewById(R.id.daily_image_view_summery_icon5);
        icons[5] = findViewById(R.id.daily_image_view_summery_icon6);
        icons[6] = findViewById(R.id.daily_image_view_summery_icon7);
        int i = 0;
        for (ImageView icon : icons) {
            if (summerySorted[i] >= userData.getGoal()) {
                icon.setImageResource(R.drawable.baseline_logout_24);
            } else {
                icon.setImageResource(R.drawable.back_btn);
            }
            i++;
        }

        TextView[] textViewSummery = new TextView[7];
        textViewSummery[0] = findViewById(R.id.daily_text_view_summery1);
        textViewSummery[1] = findViewById(R.id.daily_text_view_summery2);
        textViewSummery[2] = findViewById(R.id.daily_text_view_summery3);
        textViewSummery[3] = findViewById(R.id.daily_text_view_summery4);
        textViewSummery[4] = findViewById(R.id.daily_text_view_summery5);
        textViewSummery[5] = findViewById(R.id.daily_text_view_summery6);
        textViewSummery[6] = findViewById(R.id.daily_text_view_summery7);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
//        String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        calendar.add(Calendar.DAY_OF_YEAR, 0);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayName = dayNames[dayOfWeek - 1];
        textViewSummery[6].setText(dayName);
        for (int j = 5; j >= 0; j--) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            dayName = dayNames[dayOfWeek - 1];
            textViewSummery[j].setText(dayName);
        }
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
        if (BatteryReceiverHandler.checkBatteryLevel(this, userData.getSaveBatteryThreshold())) {
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

        stopService(new Intent(this, StepCounterService.class));
        buttonStartStop.setImageResource(android.R.drawable.ic_media_play);
    }

    @Override
    public void onStepCountChanged() {
        updateStepCount();
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
    public void updateStepCount() {
        int stepCount = userData.getStepsCounter();
        Log.d("updateStepCount", "updateStepCount: " + stepCount);
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
        setStatisticViews();
    }
}
