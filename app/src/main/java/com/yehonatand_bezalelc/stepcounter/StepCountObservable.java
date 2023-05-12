package com.yehonatand_bezalelc.stepcounter;

public interface StepCountObservable {
    void addObserver(StepCountObserver observer);

    void removeObserver(StepCountObserver observer);

    void notifyStepCountChanged(int stepCount);
}
