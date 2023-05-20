package com.yehonatand_bezalelc.stepcounter;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

public class SummeryActivity extends MainActivity {
    BarChart chart;
    Button buttonSteps, buttonDistance, buttonTime, buttonCalories;
    LinearLayout linearLayoutChart;

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

        linearLayoutChart = findViewById(R.id.summery_linear_layout_chart);
        chart = findViewById(R.id.summery_chart);
        buttonSteps = findViewById(R.id.summery_button_steps);
        buttonDistance = findViewById(R.id.summery_button_distance);
        buttonTime = findViewById(R.id.summery_button_time);
        buttonCalories = findViewById(R.id.summery_button_calories);
        updateChart();
        buttonSteps.setOnClickListener(view -> {
            updateChart();
        });
        buttonDistance.setOnClickListener(view -> {
            updateChart();
        });
        buttonTime.setOnClickListener(view -> {
            updateChart();
        });
        buttonCalories.setOnClickListener(view -> {
            updateChart();
        });
    }

    private void updateChart() {
        List<Float> dataPoints = new ArrayList<>();
        dataPoints.add(10f);
        dataPoints.add(15f);
        dataPoints.add(7f);
        dataPoints.add(12f);
        dataPoints.add(9f);
        dataPoints.add(6f);
        dataPoints.add(11f);

        List<String> labels = new ArrayList<>();
        labels.add("05-13");
        labels.add("05-14");
        labels.add("05-15");
        labels.add("05-16");
        labels.add("05-17");
        labels.add("05-18");
        labels.add("05-19");

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < dataPoints.size(); i++) {
            entries.add(new BarEntry(i, dataPoints.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Histogram");
        BarData barData = new BarData(dataSet);
        chart.setData(barData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setAxisLineColor(Color.RED);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis yAxis = chart.getAxisLeft(); // Get the Y-axis
        yAxis.setTextColor(Color.BLUE); // Set the desired color for the Y-axis labels
        chart.getAxisLeft().setAxisLineColor(Color.GREEN);

        chart.invalidate();
//        linearLayoutChart.setLay
    }
}