package com.yehonatand_bezalelc.stepcounter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private enum MenuItemType {
        about,
        setting,
        exit
    }

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        bottomNavigationView
                .setOnItemSelectedListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.daily:
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.flFragment, new DailyFragment())
                                    .commit();
                            return true;

                        case R.id.summery:
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.flFragment, new SummeryFragment())
                                    .commit();
                            return true;
                    }
                    return false;
                });
        bottomNavigationView.setSelectedItemId(R.id.daily);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(MenuItemType.about.toString());
        menu.add(MenuItemType.setting.toString());
        menu.add(MenuItemType.exit.toString());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getTitle().equals(MenuItemType.about.toString())) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("About");
//            builder.setMessage(
//                    "Step Counter\n\nBezalel Cohen 308571207\nBezalel Cohen 308571207\n" +
//                            "1.12.2023\n\n" +
//                            "System\n" +
//                            "System\n" +
//                            "System\n" +
//                            "System\n");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // Code to be executed when the positive button is clicked
//                }
//            });
////            builder.setNegativeButton("Negative Button", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    // Code to be executed when the negative button is clicked
////                }
////            });
//
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
            Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
        } else if (menuItem.getTitle().equals(MenuItemType.setting.toString())) {
            Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
        } else if (menuItem.getTitle().equals(MenuItemType.exit.toString())) {
            Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(menuItem);
    }


}
