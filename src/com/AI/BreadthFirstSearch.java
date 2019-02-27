package com.AI;

import java.util.*;
import java.lang.*;

public class BreadthFirstSearch {
    private static Set<String> visitedSet = new HashSet<>();
    private Map<String, String>  parentMap = new HashMap<>();
    private List<String> bfsQueue;
    private int N;
    public BreadthFirstSearch() {
        bfsQueue = new ArrayList<>();
    }

    public List<String> findGoalState(Puzzle puzzle) throws Exception {
        N = puzzle.getSize();
        int curI, curJ;
        String curState = puzzle.getCurrentState();
        while(!curState.equals(puzzle.getGoalState()) ) {
            curI = Helper.getSpaceIndex(curState, true);
            curJ = Helper.getSpaceIndex(curState, false);
            visitedSet.add(curState);
            if(curI -1 >= 0) {
                validateAndAdd(curState, convertTo1D(curI, curJ) ,convertTo1D(curI-1, curJ));
            }
            if(curI +1 <puzzle.getSize()) {
                validateAndAdd( curState, convertTo1D(curI, curJ) ,convertTo1D(curI+1, curJ));
            }
            if(curJ	+ 1 < puzzle.getSize()) {
                validateAndAdd( curState, convertTo1D(curI, curJ) ,convertTo1D(curI, curJ + 1));
            }
            if(curJ	- 1 >= 0) {
                validateAndAdd( curState, convertTo1D(curI, curJ) ,convertTo1D(curI, curJ - 1));
            }
            if(bfsQueue.isEmpty())
                break;
            curState = bfsQueue.remove(0);
        }

        List<String> path = new ArrayList<>();
        discoverPath(curState, path);
        return path;
    }

    private void discoverPath(String curState, List<String> path) throws Exception{
        path.add(0, curState);
        String prevState = parentMap.get(curState);
        if(prevState == null) {
            return;
        }
        discoverPath(prevState, path);
    }

    private void validateAndAdd(String curState,Integer curSpaceIndex, Integer nextSpaceIndex) throws Exception{
        String nextState = Helper.switchNumbers(curState, curSpaceIndex , nextSpaceIndex);
        if(!visitedSet.contains(nextState)) {
            bfsQueue.add(nextState);
            if( ! parentMap.containsKey(nextState)) {
                parentMap.put(nextState, curState);
            }
        }
    }


    private Integer convertTo1D(int i, int j) {
        return i*N + j;
    }
}