package com.yehonatand_bezalelc.stepcounter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("About");
            builder.setMessage(
                    "Step Counter\n\nBezalel Cohen 308571207\nBezalel Cohen 308571207\n" +
                            "1.12.2023\n\n" +
                            "System\n" +
                            "System\n" +
                            "System\n" +
                            "System\n");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Code to be executed when the positive button is clicked
                }

            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
//            Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
        } else if (menuItem.getTitle().equals(MenuItemType.setting.toString())) {
//            Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        } else if (menuItem.getTitle().equals(MenuItemType.exit.toString())) {
//            Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
//        Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO exit app
                        MainActivity.this.finish();
//                        finishAndRemoveTask();
//                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
