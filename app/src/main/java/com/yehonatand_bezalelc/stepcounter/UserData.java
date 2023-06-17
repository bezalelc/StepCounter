package com.yehonatand_bezalelc.stepcounter;


import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class UserData {

    private static UserData instance;

    public static final int MAX_HEIGHT = 230, MIN_HEIGHT = 40, MAX_WEIGHT = 300, MIN_WEIGHT = 30;
    public static final String GENERAL = "General", GOAL = "goal", HISTORY = "history", WEIGHT = "weight",
    HEIGHT = "height", SAVE_BATTERY_THRESHOLD = "battery_threshold", STEPS_COUNTER_LAST = "steps_counter_last";

    private String email, lastDay;
    private boolean count = false;
    private int goal = 5000;
    private int weight;
    private int height;
    private int saveBatteryThreshold = 30;
    private int stepsCounterLast = 0;
    private HashMap<String, Integer> history = new HashMap<>();

    public void initUserDate(int goal, int weight, int height, int saveBatteryThreshold, HashMap<String, Integer> history) {
        this.goal = goal;
        this.weight = weight;
        this.height = height;
        this.saveBatteryThreshold = saveBatteryThreshold;


        String[] last7Days = getLastWeek();
        for (String last7Day : last7Days) {
            if (history.get(last7Day) == null) {
                this.history.put(last7Day, 0);
            }
            else {
                long last7DayLong = Long.parseLong(String.valueOf(history.get(last7Day)));
                int last7DayInt = (int) (last7DayLong);
                this.history.put(last7Day, last7DayInt);
            }
        }
    }

    private UserData() {
        isTodayExist();
    }

    public Integer[] getSummerySorted() {
        String[] last7Days = getLastWeek();
        Integer[] summerySorted = new Integer[7];

        for (int i = 0; i < last7Days.length; i++) {
            summerySorted[i] = history.getOrDefault(last7Days[i], 0);
        }
        return summerySorted;
    }

    public static synchronized UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    // Getters and setters for the member variables
    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
        FirebaseAuthHelper.updateCollection(email, GENERAL, GOAL, goal);
    }

    public int getStepsCounter() {
        isTodayExist();
        String[] lastWeek;
        lastWeek = getLastWeek();
        Integer stepsCounter = history.get(lastWeek[lastWeek.length - 1]);
        if (stepsCounter == null) {
            return 0;
        }
        return stepsCounter;
    }

    public void setStepsCounter(int stepsCounterAdded) {
        int stepsCounter = getStepsCounter() + stepsCounterAdded;
        history.put(lastDay, stepsCounter);
        setHistory(history);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        FirebaseAuthHelper.updateCollection(email, GENERAL, WEIGHT, weight);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        FirebaseAuthHelper.updateCollection(email, GENERAL, HEIGHT, height);
    }

    public int getSaveBatteryThreshold() {
        return saveBatteryThreshold;
    }

    public void setSaveBatteryThreshold(int saveBatteryThreshold) {
        this.saveBatteryThreshold = saveBatteryThreshold;
        FirebaseAuthHelper.updateCollection(email, GENERAL, SAVE_BATTERY_THRESHOLD, saveBatteryThreshold);
    }

    public int getStepsCounterLast() {
        return stepsCounterLast;
    }

    public void setStepsCounterLast(int stepsCounterLast) {
        this.stepsCounterLast = stepsCounterLast;
        FirebaseAuthHelper.updateCollection(email, GENERAL, STEPS_COUNTER_LAST, stepsCounterLast);
    }

    public HashMap<String, Integer> getHistory() {
        return history;
    }

    public void setHistory(HashMap<String, Integer> history) {
        String[] last7Days = getLastWeek();
        for (String last7Day : last7Days) {
            if (history.get(last7Day) == null) {
                this.history.put(last7Day, 0);
            }
            else {
                long last7DayLong = Long.parseLong(String.valueOf(history.get(last7Day)));
                int last7DayInt = (int) (last7DayLong);
                this.history.put(last7Day, last7DayInt);
            }
        }
        FirebaseAuthHelper.updateCollection(email, GENERAL, HISTORY, history);
    }

    public void updateSteps(int stepsRegisterValue) {
        setStepsCounter(stepsRegisterValue - this.stepsCounterLast);
        setStepsCounterLast(stepsRegisterValue);
    }

    public int calculateCaloriesBurned(int steps) {
        double timeInMinutes = steps * 0.067;

        // MET value for walking (3.5 calories per kilogram per minute)
        // Calculate the calories burned using the MET formula: calories = MET * weight * time
        return (int) (3.5 * timeInMinutes * this.weight / 200);
    }

    public int stepsToDistance(int steps) {
        // Convert steps to distance in kilometers based on height and stride length
        return (int) ((steps * this.height * 0.414) / 1000.0);
    }

    public void resetValues(){
        this.email = "";
        this.lastDay = "";
        this.count = false;
        this.goal = 5000;
        this.weight = 0;
        this.height = 0;
        this.saveBatteryThreshold = 30;
        this.stepsCounterLast = 0;
        this.history = new HashMap<>();
    }

    public String[] getLastWeek(){
        // get last week in reverse order including today in the array
        String[] lastWeek = new String[7];
        Calendar calendar = Calendar.getInstance();
        Date thisDate = new Date();

        calendar.setTime(thisDate);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String today = dateFormat.format(calendar.getTime());

        for (int i = 7 - 1; i >= 0; i--) {
            lastWeek[i] = today;
            calendar.add(Calendar.DATE, -1);
            today = dateFormat.format(calendar.getTime());
        }
        return lastWeek;
    }

    public void isTodayExist() {
        Calendar calendar = Calendar.getInstance();
        Date thisDate = new Date();

        calendar.setTime(thisDate);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String today = dateFormat.format(calendar.getTime());
        if (!today.equals(lastDay)) {
            history.put(today, 0);
            lastDay = today;
        }

        if (history.size() > 7) {
            calendar.add(Calendar.DATE, -7);
            String lastWeek = dateFormat.format(calendar.getTime());
            history.remove(lastWeek);
        }
    }
}
