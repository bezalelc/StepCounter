package com.yehonatand_bezalelc.stepcounter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {

    private final UserData userData = UserData.getInstance();
    ArrayAdapter<CharSequence> adapterGoal, adapterBattery;
    private TextView heightTextViewEdit, weightTextViewEdit;

    @SuppressLint("SetTextI18n")
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

        Spinner spinnerGoal = findViewById(R.id.goal_spinner);
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

        Spinner spinnerBattery = findViewById(R.id.battery_spinner);
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

        RelativeLayout relativeLayoutHeight = findViewById(R.id.relativeLayoutHeight);
        relativeLayoutHeight.setOnClickListener(v -> showHeightDialog());
        heightTextViewEdit = findViewById(R.id.heightTextViewEdit);
        heightTextViewEdit.setText(userData.getHeight() + " cm");

        RelativeLayout relativeLayoutWeight = findViewById(R.id.relativeLayoutWeight);
        relativeLayoutWeight.setOnClickListener(v -> showWeightDialog());
        weightTextViewEdit = findViewById(R.id.weightTextViewEdit);
        weightTextViewEdit.setText(userData.getWeight() + " kg");

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

    @SuppressLint("SetTextI18n")
    private void showHeightDialog() {
        // Inflate the custom layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_height, null);
        EditText heightEditText = dialogView.findViewById(R.id.heightEditText);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Enter Height");
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Get the height value from the EditText
            String height = heightEditText.getText().toString();

            // Perform validation on the height value
            try {
                int heightValue = Integer.parseInt(height);
                if (heightValue >= UserData.MIN_HEIGHT && heightValue <= UserData.MAX_HEIGHT) {
                    userData.setHeight(Integer.parseInt(height));
                    heightTextViewEdit.setText(userData.getHeight() + " cm");
                } else {
                    // Height is outside the valid range, show an error message
                    String msg = "Height must be between " + UserData.MIN_HEIGHT + " and " + UserData.MAX_HEIGHT;
                    Toast.makeText(SettingActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                // Invalid height value, show an error message
                Toast.makeText(SettingActivity.this, "Invalid height value", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);

        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    public void showWeightDialog() {
        // Inflate the custom layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_weight, null);
        EditText weightEditText = dialogView.findViewById(R.id.weightEditText);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Enter Weight");
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Get the weight value from the EditText
            String weight = weightEditText.getText().toString();

            try {
                int weightValue = Integer.parseInt(weight);
                if (weightValue >= UserData.MIN_WEIGHT && weightValue <= UserData.MAX_WEIGHT) {
                    userData.setWeight(Integer.parseInt(weight));
                    weightTextViewEdit.setText(userData.getWeight() + " kg");
                } else {
                    // Weight is outside the valid range, show an error message
                    String msg = "Weight must be between " + UserData.MIN_WEIGHT + " and " + UserData.MAX_WEIGHT;
                    Toast.makeText(SettingActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                // Invalid weight value, show an error message
                Toast.makeText(SettingActivity.this, "Invalid weight value", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);

        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}