package com.yehonatand_bezalelc.stepcounter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public abstract class MainActivity extends AppCompatActivity {

    enum MenuItemType {
        about,
        setting,
        exit
    }

    BottomNavigationView bottomNavigationView;

    protected abstract int getLayoutResourceId();

    protected abstract int getCurrentNavBarIdSelect();

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
//
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(getCurrentNavBarIdSelect());
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (getCurrentNavBarIdSelect() != item.getItemId()) {
                switch (item.getItemId()) {
                    case R.id.summery:
                        startActivity(new Intent(getApplicationContext(), SummeryActivity.class));
                        break;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        break;
                }
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(MenuItemType.setting.toString());
        menu.add(MenuItemType.about.toString());
        menu.add(MenuItemType.exit.toString());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getTitle().equals(MenuItemType.about.toString())) {
            String osVersion = Build.VERSION.RELEASE; // Operating system version
            int sdkVersion = Build.VERSION.SDK_INT; // SDK version

            String msg = "Step Counter\n\nBezalel Cohen 308571207\nBezalel Cohen 308571207\n" +
                    "\nOperation System Information:\n" +
                    "OS Version: " + osVersion + "\n" +
                    "SDK Version: " + sdkVersion + "\n";


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("About");
            builder.setMessage(msg);
            builder.setPositiveButton("OK", (dialog, which) -> {
                // Code to be executed when the positive button is clicked
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (menuItem.getTitle().equals(MenuItemType.setting.toString())) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        } else if (menuItem.getTitle().equals(MenuItemType.exit.toString())) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> MainActivity.this.finish())
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }
}
