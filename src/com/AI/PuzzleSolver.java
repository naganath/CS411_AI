package com.AI;

import java.util.*;

public class PuzzleSolver {
    private static final Integer THRESHOLD_IN_SEC = 60*30;
    private static final Integer PUZZLE_SIZE = 4;
    private static final Map<Integer,Character> distanceMoveMap = new HashMap<>();
    private static final String SPACE = "0";
    private static final Map<String, String>  parentMap = new HashMap<>();
    private static Integer count = 0;

    private static Timer timer;

    static {
        distanceMoveMap.put(-1,'L');
        distanceMoveMap.put(1,'R');
        distanceMoveMap.put(4,'D');
        distanceMoveMap.put(-4,'U');
    }

    public static void main(String args[]) throws Exception{

        // List<List<Integer>> puzzle = getInput();
        //finalState - prevState; // moving from prev to final.
        timer = new Timer();
        timer.schedule( new ReminderTask(), THRESHOLD_IN_SEC * 1000);
        Long startTime = System.currentTimeMillis();
        Long beforeUsedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();


        Puzzle puzzle = new Puzzle(PUZZLE_SIZE);
//        List<List<Integer>> puzzle = getInput();
        puzzle.setCurrentState("5-2-4-8-10-0-3-14-13-6-11-12-1-15-9-7-");

        BreadthFirstSearch bfs = new BreadthFirstSearch();
        List<String> path =  bfs.findGoalState(puzzle);

        ItDeepeningSearch ids = new ItDeepeningSearch();
        ids.findGoalState(puzzle);

        AStar aStar = new AStar();
        aStar.findGoalState(puzzle);

        IDAStar idaStar = new IDAStar();
        idaStar.findGoalState(puzzle);

        Long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.out.println("Moves: " + printMove(path));
        System.out.println("Number of moves expanded: " + count);
        System.out.println("Time Taken: " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("Memory Used: " + ((afterUsedMem - beforeUsedMem)/1024) + " KB");

    }

    private static Integer getIndex(String str, Boolean isI) {
        String[] strNoArray = str.split("-");
        int iIndex = 0;
        for(String st : strNoArray) {
            if(st.equals(SPACE)) {
                break;
            } iIndex++;
        }
        return isI ? iIndex /4 : iIndex % 4;
    }


    private static String printMove(List<String> path) throws Exception {
        StringBuilder moves = new StringBuilder();
        for (int i = 1; i < path.size(); i++) {
            String prevState = path.get(i-1);
            String curState = path.get(i);
            Integer finalI = getIndex(curState, true), finalJ = getIndex(curState, false);
            Integer prevI = getIndex(prevState, true), prevJ = getIndex(prevState, false);
            Integer finalSpaceIndex = convertTo1D(finalI, finalJ);
            Integer prevSpaceIndex = convertTo1D(prevI, prevJ);
            moves.append(prevState).append(distanceMoveMap.get(finalSpaceIndex - prevSpaceIndex) + "\n");
        }
        return moves.toString();
    }

    private static Integer convertTo1D(int i, int j) {
        return i* PUZZLE_SIZE + j;
    }

    static class  ReminderTask extends TimerTask {
        public  void run() {
            System.out.println("Solution cannot be found");
            timer.cancel();
            System.exit(0);
        }
    }
}
