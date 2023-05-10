package com.yehonatand_bezalelc.stepcounter;

import android.os.Bundle;


public class HomeActivity extends MainActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    protected int getCurrentNavBarIdSelect() {
        return R.id.home;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
