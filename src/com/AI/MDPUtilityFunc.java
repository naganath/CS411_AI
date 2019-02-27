package com.AI;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MDPUtilityFunc {
    private  static final String IN_FILENAME = "input.txt";
    private  static final String OUT_FILENAME = "output.txt";
    private static Double[][] matrix;
    private static double reward;
    private static double discountRate;
    private static double epsilon;
    private static List<Double> transProbilities = new ArrayList<>();
    private static int row = -1, col = -1;
    private static List<Pair> terminalStates = new ArrayList<>();
    private static Double delta = Double.MIN_VALUE;

    private static boolean isTerminalState(int i, int j) {
        return terminalStates.contains(new Pair(i,j));
    }

    public static  void  main(String args[]) throws  Exception{
        List<String> input = readFile();
        setMatrixSize(input.get(0));
        setMatrixDefault();
        setWalls(input.get(1));
        setTerminalStates(input.get(2));
        reward = getDoubleValue(input.get(3));
        setTransProbilities(input.get(4));
        discountRate = getDoubleValue(input.get(5));
        epsilon = getDoubleValue(input.get(6));


        FileWriter fw = new FileWriter(OUT_FILENAME);

        Double[][] oldMatrix = new Double[row][col];
        int it = 0;

        for( int i =0 ;i<row; i++) {
            for (int j = 0; j < col; j++) {
                if (isTerminalState(i, j)) {
                    oldMatrix[i][j] = 0.0;
                    continue;
                }
                oldMatrix[i][j] = matrix[i][j];
            }
        }

        do {
            delta = 0.0;
                // Processing states !
            for (int i = 0; i < row; i++)
                for (int j = 0; j < col; j++) {
                    if (oldMatrix[i][j] == null) {
//                        oldMatrix[i][j] = matrix[i][j];
                        matrix[i][j] = oldMatrix[i][j];
                        continue;
                    }
                    if( isTerminalState(i,j)) continue;
                    Double up = transProbilities.get(0) * getUtility(oldMatrix, i, j, i + 1, j) +
                            transProbilities.get(1) * getUtility(oldMatrix, i, j, i, j - 1) +
                            transProbilities.get(2) * getUtility(oldMatrix, i, j, i, j + 1) +
                            transProbilities.get(3) * getUtility(oldMatrix, i, j, i - 1, j);

                    Double down = transProbilities.get(0) * getUtility(oldMatrix, i, j, i - 1, j) +
                            transProbilities.get(1) * getUtility(oldMatrix, i, j, i, j + 1) +
                            transProbilities.get(2) * getUtility(oldMatrix, i, j, i, j - 1) +
                            transProbilities.get(3) * getUtility(oldMatrix, i, j, i + 1, j);

                    Double left = transProbilities.get(0) * getUtility(oldMatrix, i, j, i, j - 1) +
                            transProbilities.get(1) * getUtility(oldMatrix, i, j, i - 1, j) +
                            transProbilities.get(2) * getUtility(oldMatrix, i, j, i + 1, j) +
                            transProbilities.get(3) * getUtility(oldMatrix, i, j, i, j + 1);

                    Double right = transProbilities.get(0) * getUtility(oldMatrix, i, j, i, j + 1) +
                            transProbilities.get(1) * getUtility(oldMatrix, i, j, i + 1, j) +
                            transProbilities.get(2) * getUtility(oldMatrix, i, j, i - 1, j) +
                            transProbilities.get(3) * getUtility(oldMatrix, i, j, i, j - 1);
                    Double max = Math.max(up, Math.max(down, Math.max(left, right)));
                    matrix[i][j] = max * discountRate + reward;

                }


            for (int i = 0; i < row; i++)
                for (int j = 0; j < col; j++)
                    if (matrix[i][j] != null && Math.abs(matrix[i][j] - oldMatrix[i][j]) > delta) {
                        delta = Math.abs(matrix[i][j] - oldMatrix[i][j]);
                    }

            printIteration(fw, it, oldMatrix);
            it++;

            for( int i =0 ;i<row; i++)
                for (int j = 0; j < col; j++)
                    oldMatrix[i][j] = matrix[i][j];

        }while ( delta > epsilon * (1 - discountRate)/ discountRate );

        printIteration(fw, it, oldMatrix);
        fw.flush();
        fw.close();
    }

    private static void printIteration(FileWriter fw, int it, Double[][] oldMatrix) throws Exception {
        fw.write("\nIteration: " + it);
        it++;
        for( int i =0 ;i<row; i++) {
            fw.write("\n");
            for (int j = 0; j < col; j++) {
                fw.write(" " +  String.valueOf(oldMatrix[i][j]));
            }
        }
    }

    private static Double getUtility(Double[][] oldMatrix, int curI, int curJ, int i, int j) {
        i = i< 0 ? 0: i;
        i = i == row? row-1 : i;
        j = j < 0  ? 0 : j;
        j = j == col ? col -1  : j;
        if(oldMatrix[i][j] == null)
            return oldMatrix[curI][curJ];
        return oldMatrix[i][j];
    }

    private static void setMatrixDefault() {
        row = matrix.length;
        col = matrix[1].length;
        for(int i = 0 ;i <row; i++) {
            for( int j = 0 ; j<col ; j++) {
                matrix[i][j] = 0.0;
            }
        }
    }

    private static void setTransProbilities(String data) {
        String[]  probList = data.split(":")[1].split(" ");
        for (String prob : probList) {
            if(prob.trim().equals(""))
                continue;
            transProbilities.add(Double.valueOf(prob.trim()));
        }
    }

    private static Double getDoubleValue(String data) {
        return Double.valueOf(data.split(":")[1].trim());
    }

    private static void setTerminalStates(String data) {
        String terminalStrList[] = data.split(":")[1].split(",");
        for(String terminalStr  : terminalStrList) {
            String terminalVals[] = terminalStr.split(" ");
            int x = -1, y = -1;
            Double val = -1.0;
            for(String terminalId : terminalVals) {
                if(terminalId.trim().equals(""))
                    continue;
                if(x == -1) {
                    x = Integer.valueOf(terminalId.trim());
                    continue;
                }
                if( y  == -1) {
                    y =  Integer.valueOf(terminalId.trim());
                    continue;
                }
                val = Double.valueOf(terminalId.trim());
            }
            x--;
            y--;
            matrix[x][y] = val;
            terminalStates.add(new Pair(x,y));

        }
    }

    private static void setWalls(String data) {
        String wallStrList[] = data.split(":")[1].split(",");
        for(String wallStr : wallStrList) {
            String wallLoc[] = wallStr.split(" ");
            int x = -1, y = -1;
            for(String wallId : wallLoc) {
                if(wallId.trim().equals("")) {
                    continue;
                }
                if(x == -1)
                    x = Integer.valueOf(wallId.trim());
                else
                    y = Integer.valueOf(wallId.trim());
            }
            x--;y--;
            matrix[x][y] = null;
        }
    }

    private static void setMatrixSize(String data) {
        int[] sizeArr = new int[2];
        String[] size =  data.split(":")[1].split(" ");
        sizeArr[0] = Integer.valueOf(size[1]);
        sizeArr[1] = Integer.valueOf(size[2]);
//        int[] sizeArr = getSize(input.get(0));
        matrix = new Double[sizeArr[0]][sizeArr[1]];
    }

    private static List<String> readFile() throws  Exception{
        List<String> data = new ArrayList<>();
        BufferedReader br =  new BufferedReader(new InputStreamReader(new FileInputStream(IN_FILENAME)));
        String tmp = null;
        while((tmp = br.readLine())!= null) {
            if(tmp.equals("") || tmp.charAt(0) == '#')
                continue;
            data.add(tmp);
        }
        return data;
    }
}

class Pair {
    private int x;
    private int y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        Pair p = (Pair)o;
        return  this.x == p.getX() && this.y == p.getY();
    }

    @Override
    public int hashCode() {
        return (x*31 + y * 23 ) / 119;
    }
}
