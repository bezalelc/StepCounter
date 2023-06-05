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
}


//  count:
//    stepsCounterLast=counter
//  counter update:
//    stepsCounter=counter-stepsCounterLast
//    stepsCounterLast=counter}