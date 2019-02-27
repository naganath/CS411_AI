package com.AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
Iterative Deepening Search
 */

public class ItDeepeningSearch {
    private int N;


    public List<String> findGoalState(Puzzle puzzle) throws Exception{

        N = puzzle.getSize();
        List<String> pathList;
        int limit = 0;
        while(true) {
            limit++;
            System.out.println("Limit : " + limit);
            pathList = IDS(puzzle, limit);
            if(!pathList.isEmpty())  {
                break;
            }
        }
        return pathList;
    }


    private List<String> IDS(Puzzle puzzle, Integer limit) throws Exception {
        String curState = puzzle.getCurrentState();
        if(curState.equals(puzzle.getGoalState())) {
            return  Arrays.asList(curState);
        }

        if(limit == 0) {
            return Collections.EMPTY_LIST;
        }
        List<String> resultStrList = new ArrayList<>();
        int curI, curJ;
        curI = Helper.getSpaceIndex(curState, true);
        curJ = Helper.getSpaceIndex(curState, false);
        Puzzle puzzle1 = new Puzzle(puzzle.getSize());

        if(curI -1 >= 0) {

            String nextNode = Helper.switchNumbers(curState, convertTo1D(curI, curJ), convertTo1D(curI -1, curJ));
            puzzle1.setCurrentState(nextNode);
            resultStrList.addAll(IDS(puzzle1,limit -1));
        }
        if( resultStrList.isEmpty() && curI +1 <N) {

            String nextNode = Helper.switchNumbers(curState, convertTo1D(curI, curJ), convertTo1D(curI +1, curJ));
            puzzle1.setCurrentState(nextNode);
            resultStrList.addAll(IDS(puzzle1,limit -1));
        }
        if( resultStrList.isEmpty() &&curJ	+ 1 < N) {
            String nextNode = Helper.switchNumbers(curState, convertTo1D(curI, curJ), convertTo1D(curI, curJ+1));
            puzzle1.setCurrentState(nextNode);
            resultStrList.addAll(IDS(puzzle1,limit -1));
        }
        if( resultStrList.isEmpty() &&curJ	- 1 >= 0) {
            String nextNode = Helper.switchNumbers(curState, convertTo1D(curI, curJ), convertTo1D(curI, curJ-1));
            puzzle1.setCurrentState(nextNode);
            resultStrList.addAll(IDS(puzzle1,limit -1));
        }
        if(!resultStrList.isEmpty()) {
            resultStrList.add(curState);
        }
        return resultStrList;
    }

    private Integer convertTo1D(int i, int j) {
        return i*N + j;
    }
}