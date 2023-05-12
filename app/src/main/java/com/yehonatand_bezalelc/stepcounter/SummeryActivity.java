package com.yehonatand_bezalelc.stepcounter;

import android.os.Bundle;

public class SummeryActivity extends MainActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_summery;
    }

    @Override
    protected int getCurrentNavBarIdSelect() {
        return R.id.summery;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}