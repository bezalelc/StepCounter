package com.yehonatand_bezalelc.stepcounter;


import java.util.HashMap;

public class UserData {

    //    todo: 1. add notification in day start

    private static UserData instance;

    public static final int MAX_HEIGHT = 230, MIN_HEIGHT = 40, MAX_WEIGHT = 300, MIN_WEIGHT = 30;
    public static final String GENERAL = "General",
    STEP_COUNTER = "step_counter", GOAL = "goal",
    WEIGHT = "weight", HEIGHT = "height", SAVE_BATTERY_THRESHOLD = "battery_threshold", STEPS_COUNTER_LAST = "steps_counter_last";

    private String email, password;
    private boolean count = false, notification = true;
    private int goal = 5000;
    private int stepsCounter = 0;
    private int weight;
    private int height;
    private int saveBatteryThreshold = 30;
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
        Integer[] summerySorted = new Integer[7];
        // todo yona: get sorted according to history
        summerySorted[0] = 5000;
        summerySorted[1] = 5000;
        summerySorted[2] = 5000;
        summerySorted[3] = 5000;
        summerySorted[4] = 7000;
        summerySorted[5] = 5000;
        summerySorted[6] = 200;//today
        return summerySorted;
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

    public int getStartTimeM() {
        return startTimeM;
    }

    // todo
    public void setStartTime(int h, int m) {
        this.startTimeH = h;
        this.startTimeM = m;
    }

    public boolean isCount() {
        return count;
    }

    // todo firebase or local file
    public void setCount(boolean count) {
        this.count = count;
    }

    public boolean isNotification() {
        return notification;
    }

    // todo
    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
        FirebaseAuthHelper.updateCollection(email, GENERAL, GOAL, goal);
    }

    public int getStepsCounter() {
        return stepsCounter;
    }

    public void setStepsCounter(int stepsCounterAdded) {
        this.stepsCounter += stepsCounterAdded;
        FirebaseAuthHelper.updateCollection(email, GENERAL, STEP_COUNTER,
                this.stepsCounter + stepsCounterAdded);
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

    // todo maybe change hashmap
    public void setHistory(HashMap<String, Integer> history) {
        this.history = history;
    }

    public void updateSteps(int stepsRegisterValue) {
        //  todo: 3. add new day data when day change according to local HH:MM
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
        this.password = "";
        this.count = false;
        this.notification = true;
        this.goal = 5000;
        this.stepsCounter = 0;
        this.weight = 0;
        this.height = 0;
        this.saveBatteryThreshold = 30;
        this.stepsCounterLast = 0;
        this.startTimeH = 8;
        this.startTimeM = 0;
    }
}
