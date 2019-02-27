package com.AI;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    private static final String SPACE = "0";


    public static Integer getSpaceIndex(String state, Boolean isX) {
        String[] strNoArray = state.split("-");
        int iIndex = 0;
        for(String st : strNoArray) {
            if(st.equals(SPACE)) {
                break;
            } iIndex++;
        }
        return isX ? iIndex /4 : iIndex % 4;
    }

    public static List<List<Integer>> reverseDecode(String state) {
        List<List<Integer>> puzzle = new ArrayList<>();
        int N = getSize(state);
        for(int i =0 ; i<N;i++) {
            puzzle.add(new ArrayList<>());
        }
        String[] strNoArray = state.split("-");
        for( int i =0 ; i < strNoArray.length;i++) {
            puzzle.get(i/N).add(Integer.valueOf(strNoArray[i]));
        }
        return puzzle;
    }

    public static Integer getSize(String state) {
        return (int)Math.sqrt(state.split("-").length);
    }


    public static String switchNumbers(String state, Integer fromIndex, Integer toIndex) throws Exception{

        int N = getSize(state);
        int fromI = fromIndex / N, fromJ = fromIndex % N;
        int toI = toIndex /N , toJ = toIndex % N;
        List<List<Integer>> newPuzzle = reverseDecode(state);
        int temp =  newPuzzle.get((fromI)).remove(fromJ);
        newPuzzle.get(fromI).add(fromJ, newPuzzle.get(toI).get(toJ));
        newPuzzle.get(toI).remove(toJ);
        newPuzzle.get(toI).add(toJ, temp);
        return  getUniqueCode(newPuzzle);
    }


    private static  String getUniqueCode(List<List<Integer>> puzzle) throws Exception{
        int N = puzzle.size();
        String str = "";
        for(int i =0 ; i<N;i++) {
            for(int j =0 ; j< N;j++) {
                str += puzzle.get(i).get(j) + "-";
            }
        }
        return str;
    }

}
