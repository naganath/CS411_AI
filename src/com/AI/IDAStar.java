package com.AI;

import java.util.*;

/*
IDA* with Manhattan Distance and MisplacedTiles Heuristics
 */

public class IDAStar {
    private Integer N ;
    private static final String SPACE = "0";
    private static Queue<String> minPriorityQueue = new PriorityQueue<>();
    private static Set<String> visitedList = new HashSet<>();
    private static Map<String,String> parentMap = new HashMap<>();

    public static Comparator<String> manhattanHeuristics = new Comparator<String>(){

        @Override
        public int compare(String str1, String str2) {
            return util.getManhattanDistance(str1) - util.getManhattanDistance(str2);
        }
    };

    public static Comparator<String> misplacedHeuristics = new Comparator<String>(){

        @Override
        public int compare(String str1, String str2) {
            return util.getMisplacedTiles(str1) - util.getMisplacedTiles(str2);
        }
    };

    private List<String> getPath(Map<String, String> parentMap, String finalState) {
        List<String> pathList = new ArrayList<>();
        String curState  = finalState;
        while (curState != null) {
            pathList.add(curState);
            curState = parentMap.get(curState);
        }
        return pathList;
    }

    public List<String> findGoalState(Puzzle puzzle) throws Exception{
        N = puzzle.getSize();
        Integer bound = 0;
        int count = 0;
        do {
            count++;
            visitedList = new HashSet<>();
            parentMap = new HashMap<>();
            bound= IDAStar(puzzle, true, bound);
            System.out.println(" count1 " + count + " bound " + bound);
        }while (bound != null);
        System.out.println(" count " + count);
        List<String> pathList = getPath(parentMap, puzzle.getGoalState());
        return pathList;
    }

    private void validateAndAdd(String nextState, String curState) {
        if(!visitedList.contains(nextState)) {
            parentMap.put(nextState, curState);
            minPriorityQueue.add(nextState);
        }
    }

    private Integer IDAStar(Puzzle puzzle, Boolean isManhattan, int bound) throws  Exception {
        minPriorityQueue = isManhattan? new PriorityQueue<>(manhattanHeuristics) : new PriorityQueue<>(misplacedHeuristics);
        int minVal = Integer.MAX_VALUE;
        String curState = null;
        int curI, curJ;
        parentMap.put(puzzle.getCurrentState(), null);
        minPriorityQueue.add(puzzle.getCurrentState());
        while(!minPriorityQueue.isEmpty()) {

            curState = minPriorityQueue.poll();

            visitedList.add(curState);
            Integer curStateValue = isManhattan ? util.getManhattanDistance(curState) : util.getMisplacedTiles(curState);
            if(curStateValue > bound) {
                minVal = minVal > curStateValue ? curStateValue : minVal;
                continue;
            }
            if(curState.equals(puzzle.getGoalState())) {
                return null;
            }
            curI = Helper.getSpaceIndex(curState, true);
            curJ = Helper.getSpaceIndex(curState, false);

            if(curI - 1 >= 0) {
                String nextState = Helper.switchNumbers(curState,convertTo1D(curI, curJ),
                        convertTo1D(curI -1, curJ));
                validateAndAdd(nextState,curState);

            }
            if(curI + 1 < N) {
                String nextState = Helper.switchNumbers(curState,convertTo1D(curI, curJ),
                        convertTo1D(curI + 1, curJ));
                validateAndAdd(nextState,curState);
            }
            if(curJ - 1 >= 0) {
                String nextState = Helper.switchNumbers(curState, convertTo1D(curI, curJ),
                        convertTo1D(curI, curJ -1));
                validateAndAdd(nextState,curState);
            }
            if(curJ + 1 < N) {
                String nextState = Helper.switchNumbers(curState, convertTo1D(curI, curJ),
                        convertTo1D(curI, curJ +1));
                validateAndAdd(nextState,curState);
            }
        }

        return  minVal;
    }


    private Integer convertTo1D(int i, int j) {
        return i*N + j;
    }


    static class util {
        private static int getManhattanDistance(String state) {
            String[] strNoArray = state.split("-");
            int size = Double.valueOf(Math.sqrt(strNoArray.length)).intValue();
            int sum = 0;
            for( int i =0; i<strNoArray.length; i++) {
//                 i --> current Location.   no --> expected location.
//                 0 1 2 3 --> 1               4 5 6 7   --> 2
//                 0 4 8 12  --> 0    1 5 9 13  --> 1
                int curI = i / size + 1;
                int curJ = i % size + 1;
                int no = Integer.valueOf(strNoArray[i]);
                no = no == 0 ? 16 : no;

                int noI = (no-1)/ size + 1;
                int noJ = (no-1)% size + 1;
                int distance = Double.valueOf(Math.pow(curI - noI,2) + Math.pow(curJ - noJ,2)).intValue();
                sum += distance;
            }
            return sum;
        }

        private static int getMisplacedTiles(String state) {
            String[] strNoArray = state.split("-");
            int count = 0;
            for( int i =1; i<=strNoArray.length; i++) {
                if(i%strNoArray.length != Integer.valueOf(strNoArray[i-1]))
                    count++;
            }
            return count;
        }
    }
}



