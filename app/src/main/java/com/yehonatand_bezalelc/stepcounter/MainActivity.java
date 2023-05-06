package com.yehonatand_bezalelc.stepcounter;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);
        DailyFragment dailyFragment = new DailyFragment();
        SummeryFragment summeryFragment = new SummeryFragment();

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


}
