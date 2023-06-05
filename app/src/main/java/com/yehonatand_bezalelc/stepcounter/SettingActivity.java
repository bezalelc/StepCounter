package com.yehonatand_bezalelc.stepcounter;

import android.app.TimePickerDialog;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.widget.SwitchCompat;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {

    private final UserData userData = UserData.getInstance();
    private Spinner spinnerGoal, spinnerBattery;
    ArrayAdapter<CharSequence> adapterGoal, adapterBattery;
    private SwitchCompat switchCompatNotification;
    private TextView textVieweStart;
    private RelativeLayout relativeLayoutStartTime, relativeLayoutNotification;
    private final int TIME_START_MAX_LENGTH = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (!FirebaseAuthHelper.isUserConnected()) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        ImageButton settingBackButton = findViewById(R.id.settingBackButton);
        RelativeLayout logoutButton = findViewById(R.id.logout);

        spinnerGoal = findViewById(R.id.goal_spinner);
        adapterGoal = ArrayAdapter.createFromResource(this, R.array.step_goal_list, android.R.layout.simple_spinner_item);
        adapterGoal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGoal.setAdapter(adapterGoal);
        spinnerGoal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                userData.setGoal(Integer.parseInt(selectedItem));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerGoal.setSelection((userData.getGoal() / 500) - 1);

        spinnerBattery = findViewById(R.id.battery_spinner);
        adapterBattery = ArrayAdapter.createFromResource(this, R.array.battery_percents_list, android.R.layout.simple_spinner_item);
        adapterBattery.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBattery.setAdapter(adapterBattery);
        spinnerBattery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                userData.setSaveBatteryThreshold(Integer.parseInt(selectedItem));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerBattery.setSelection((userData.getSaveBatteryThreshold() / 5) - 1);

        switchCompatNotification = findViewById(R.id.switchCompat);
        switchCompatNotification.setChecked(userData.isNotification());
//        relativeLayoutNotification=findViewById(R.id.relativeLayoutNotification);
        switchCompatNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle switch state changes
            userData.setNotification(isChecked);
        });

        textVieweStart = findViewById(R.id.timeEditText);
        textVieweStart.setText(userData.getStartTimeStr());
        relativeLayoutStartTime = findViewById(R.id.relativeLayoutStartTime);
        relativeLayoutStartTime.setOnClickListener(v -> showTimePickerDialog());

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

        settingBackButton.setOnClickListener(v -> {
            startActivity(new Intent(SettingActivity.this, HomeActivity.class));
            finish();
        });
    }

    private void showTimePickerDialog() {
//        Calendar currentTime = Calendar.getInstance();
        int hour = userData.getStartTimeH();
        int minute = userData.getStartTimeM();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                userData.setStartTime(hourOfDay, minute);
                textVieweStart.setText(userData.getStartTimeStr());
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

}