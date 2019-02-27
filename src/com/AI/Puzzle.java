package com.AI;

import java.util.ArrayList;
import java.util.List;

public class Puzzle {

    private Integer size;
    public String goalState;
    private String currentState;
    private Integer expandedCount;

    public Puzzle(Integer size) {
        this.size = size;
        this.expandedCount = 0;
        generateGoalState();
    }

    private void generateGoalState() {
        StringBuilder goal = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int no = (i*4 + j + 1) % (size*size);
                goal.append(no).append("-");
            }
        }
        goalState = goal.toString();
    }

    public Integer getExpandedCount() {
        return expandedCount;
    }

    public void setExpandedCount(Integer expandedCount) {
        this.expandedCount = expandedCount;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getGoalState() {
        return goalState;
    }

    public void setGoalState(String goalState) {
        this.goalState = goalState;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
}
