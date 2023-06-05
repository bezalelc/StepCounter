package com.yehonatand_bezalelc.stepcounter;


import java.util.HashMap;

public class UserData {
    private static UserData instance;

    public static final int MAX_HEIGHT = 230, MIN_HEIGHT = 40, MAX_WEIGHT = 300, MIN_WEIGHT = 30;

    private String email, password;
    private boolean count = false, notification = true;
    private int goal = 5000;
    private int stepsCounter = 0;
    private int weight = 75;
    private int height = 175;
    private int saveBatteryThreshold = 5;
    private int stepsCounterLast = 0;
    private int startTimeH = 8;
    private int startTimeM = 0;
    private HashMap<String, Integer> history = new HashMap<>();

    private UserData() {
        history.put("05-13", 5500);
        history.put("05-14", 4000);
        history.put("05-15", 1500);
        history.put("05-16", 4567);
        history.put("05-17", 6000);
        history.put("05-18", 11000);
        history.put("05-19", 400);
        history.put("05-20", 400);
    }

    public Integer[] getSummerySorted() {
        Integer[] summerySortedReversed = new Integer[8];
        summerySortedReversed[0] = 5000;
        summerySortedReversed[1] = 5000;
        summerySortedReversed[2] = 5000;
        summerySortedReversed[3] = 5000;
        summerySortedReversed[4] = 7000;
        summerySortedReversed[5] = 5000;
        summerySortedReversed[6] = 5000;
        summerySortedReversed[7] = 200;
        return summerySortedReversed;
    }

    public static synchronized UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    // Getters and setters for the member variables

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStartTimeStr() {
        String timeStr = "";
        if (startTimeH < 10) {
            timeStr += "0";
        }
        timeStr += startTimeH + ":";
        if (startTimeM < 10) {
            timeStr += "0";
        }
        timeStr += startTimeM;


        return timeStr;
    }

    public int getStartTimeH() {
        return startTimeH;
    }

    public void setStartTimeH(int startTimeH) {
        this.startTimeH = startTimeH;
    }

    public int getStartTimeM() {
        return startTimeM;
    }

    public void setStartTimeM(int startTimeM) {
        this.startTimeM = startTimeM;
    }

    public void setStartTime(int h, int m) {
        this.startTimeH = h;
        this.startTimeM = m;
    }

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getStepsCounter() {
        return stepsCounter;
    }

    public void setStepsCounter(int stepsCounterAdded) {
        this.stepsCounter += stepsCounterAdded;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSaveBatteryThreshold() {
        return saveBatteryThreshold;
    }

    public void setSaveBatteryThreshold(int saveBatteryThreshold) {
        this.saveBatteryThreshold = saveBatteryThreshold;
    }

    public int getStepsCounterLast() {
        return stepsCounterLast;
    }

    public void setStepsCounterLast(int stepsCounterLast) {
        this.stepsCounterLast = stepsCounterLast;
    }

    public HashMap<String, Integer> getHistory() {
        return history;
    }

    public void setHistory(HashMap<String, Integer> history) {
        this.history = history;
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

    public int getLastWeakAverage() {
        if (history.isEmpty()) {
            return 0; // Return 0 if the HashMap is empty to avoid division by zero
        }

        int sum = 0;
        for (int value : history.values()) {
            sum += value;
        }

        return sum / history.size();
    }

//    private double stepsToTime(int steps) {
//        // Convert steps to time in minutes
//        // This is a rough estimate and can vary depending on individuals
//        return steps * 0.067;
//    }

}


//  count:
//    stepsCounterLast=counter
//  counter update:
//    stepsCounter=counter-stepsCounterLast
//    stepsCounterLast=counter}