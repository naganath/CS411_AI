package com.AI;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;


public class DecisionTree {

    enum FeatureAttribute
    {
        Alternate, Bar, Fri_Sat, Hungry, Patrons, Price, Raining, Reservation, Type, WaitEstimate, WillWait;
    }

    private Map<FeatureAttribute, List<String>> getInput() throws Exception {
        Map<FeatureAttribute, List<String>> dataSet = new HashMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("input.csv")));
        String inputLineStr =  null;
        while( (inputLineStr = br.readLine()) != null) {
            String inputData[] =  inputLineStr.split(",");
            int i = 0;
            for(FeatureAttribute f : FeatureAttribute.values()) {
                if(!dataSet.containsKey(f)) {
                    dataSet.put(f, new ArrayList<>());
                }
                dataSet.get(f).add(inputData[i].trim());
                i++;
            }
        }
        return dataSet;

    }

    private List<Integer> getRowIndex(List<String> strList, String value) {
        List<Integer> rowList = new ArrayList<>();
        int i = 0;
        for(String str : strList) {
            if(str.equals(value)) {
                rowList.add(i);
            }
            i++;
        }
        return rowList;
    }

    private String  getPluralityValue (List<String> domainValues) {
        Random rand = new Random();
        Map<String,Integer> countMap = new HashMap<>();
        for(String str : domainValues) {
            if(!countMap.containsKey(str)) {
                countMap.put(str, 0);
            }
            countMap.put(str, countMap.get(str) +1);
        }
        int max = -1;
        String retStr = null;
        for(Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if(max == entry.getValue()) {
                 retStr =  rand.nextBoolean() ? entry.getKey() : retStr;
            }
            if(max < entry.getValue()) {
                max  = entry.getValue();
                retStr = entry.getKey();
            }
        }
        return  retStr;
    }

    private TreeNode DFSTreeNode(Map<FeatureAttribute, List<String>> dataSet) {
        System.out.println(" new Iteration");
        // recursive terminal statement when no attribute is present.
        if(dataSet.size() == 1) {
            TreeNode node = new TreeNode(getPluralityValue(dataSet.get(FeatureAttribute.WillWait)),true);
            return node;
        }

        if(isPure(dataSet.get(FeatureAttribute.WillWait))) {
            TreeNode node = new TreeNode(dataSet.get(FeatureAttribute.WillWait).get(0),true);
            return node;
        }


        Double maxInfoGain = -Double.MAX_VALUE;
        double targetEntropy = getEntropy(dataSet.get(FeatureAttribute.WillWait));
        FeatureAttribute maxGainFeatureAttribute = null;
        for(FeatureAttribute f : dataSet.keySet()) {
            if(f.equals(FeatureAttribute.WillWait)) {
                continue;
            }
            Double infoGain_i = getInfoGain(targetEntropy, dataSet.get(f), dataSet.get(FeatureAttribute.WillWait));
            System.out.println(f + " " + infoGain_i);
            if(maxInfoGain == null || maxInfoGain < infoGain_i) {
                maxGainFeatureAttribute = f;
                maxInfoGain = infoGain_i;
            }
        }
        TreeNode rootNode = new TreeNode(maxGainFeatureAttribute.toString(), false) ;
        Set<String> featureDomain = new HashSet<>(dataSet.get(maxGainFeatureAttribute));
        for(String domainValue : featureDomain) {

            Map<FeatureAttribute, List<String>> filteredDataSet = clone(dataSet);
            List<Integer> rowIndex = getRowIndex(dataSet.get(maxGainFeatureAttribute), domainValue);
            filterRows(filteredDataSet, rowIndex);
            filteredDataSet.remove(maxGainFeatureAttribute);

            TreeNode node =  DFSTreeNode( filteredDataSet);
            rootNode.getChildNodes().put(domainValue, node);
        }

        return rootNode;
    }

    private void filterRows(Map<FeatureAttribute, List<String>> dataSet, List<Integer> rowList) {
        for(FeatureAttribute f : dataSet.keySet()) {
            List<String> filteredValues = new ArrayList<>();
            for(Integer row : rowList) {
                filteredValues.add(dataSet.get(f).get(row));
            }
            dataSet.put(f,filteredValues);
        }
    }

    private boolean isPure( List<String> resultList) {
        String str = resultList.get(0);
        for (String compareStr : resultList) {
            if (!compareStr.equals(str))
                return false;
        }
        return true;
    }

    public  void execute() throws Exception {
        Map<FeatureAttribute, List<String>> dataSet = getInput();
        TreeNode root = DFSTreeNode(dataSet);
        printTree3(null, root, 0);
        System.out.println(" New Print ");
        printTree2(null, root, 0);
        System.out.println(" New Print ");
        printTree1(null, root, 0);
        System.out.println(" New Print ");
        printTree(null, root, 0);
        System.out.println("");
    }

    private double Log2(double value) {
        return Math.log(value) / Math.log(2);
    }

    private double getInfoGain(double rootEntropy, List<String> featureValues, List<String> targetFeatureValues) {
        Set<String> domainValues = new HashSet<>(featureValues);
        Double totalEntropy = 0.0;
        int size = featureValues.size();
        for(String domain : domainValues) {
            List<String> targetSubset = new ArrayList<>();
            int domainCount = 0;
            int i = 0;
            for(String str : featureValues) {
                if(str.equals(domain)) {
                    targetSubset.add(targetFeatureValues.get(i));
                    domainCount++;
                }
                i++;
            }
            double domainEntropy = domainCount/(double)size *getEntropy(targetSubset);
            totalEntropy += domainEntropy;
        }
        return  rootEntropy - totalEntropy;
    }

    // sum of -P * log2(P)
    private double getEntropy(List<String> featureValues) {
        Map<String, Integer> valueCountMap = new HashMap<>();

        for(String str : featureValues ) {
            if(!valueCountMap.containsKey(str)) {
                valueCountMap.put(str,0);
            }
            valueCountMap.put(str, valueCountMap.get(str) + 1);
        }
        int size = featureValues.size();
        double entropy = 0.0;
        for(Map.Entry<String, Integer> valueCountEntry : valueCountMap.entrySet()) {
            double prob = valueCountEntry.getValue() /(double)size;
            double valueEntropy = -prob*Log2(prob);
            entropy += valueEntropy;
        }
        return entropy;
    }

    private Map<FeatureAttribute,List<String>> clone(Map<FeatureAttribute, List<String>> featureListMap) {
        Map<FeatureAttribute,List<String>> featureMap = new HashMap<>();
        for(Map.Entry<FeatureAttribute, List<String>> entry : featureListMap.entrySet()) {
            featureMap.put(entry.getKey(),clone(entry.getValue()));
        }
        return featureMap;
    }

    private List<String> clone(List<String> strList) {
        List<String> arr = new ArrayList<>();
        for(String str: strList) {
            arr.add(str);
        }
        return arr;
    }

    private void printTree(String domainValue, TreeNode node, int spacing) {
        System.out.println("");
        for(int i = 0; i<spacing; i++) {
            System.out.print("\t\t");
        }
        if(domainValue !=null) {
            System.out.print(  domainValue + "-->");
        }
        System.out.print(node.getData());
        for(Map.Entry<String, TreeNode> entry : node.getChildNodes().entrySet()) {
            printTree(entry.getKey(), entry.getValue(), spacing + 1);
        }
    }

    private void printTree1(String domainValue, TreeNode node, int spacing) {
        System.out.print("\n");
        for(int i = 0; i<spacing-1; i++) {
            System.out.print("|\t");
        }
        if(spacing > 0)
            System.out.print("|---");
        if(domainValue !=null) {
            System.out.print(  domainValue + " ==>>>");
        }
        System.out.print(node.getData());
        for(Map.Entry<String, TreeNode> entry : node.getChildNodes().entrySet()) {
            printTree1(entry.getKey(), entry.getValue(), spacing + 1);
        }
    }

    private void printTree2(String domainValue, TreeNode node, int spacing) {
        System.out.print("\n");
        for(int i = 0; i<spacing-1; i++) {
            System.out.print("+\t");
        }
        if(spacing > 0)
            System.out.print("+---");
        if(domainValue !=null) {
            System.out.print(  domainValue + " >>>>>");
        }
        System.out.print(node.getData());
        for(Map.Entry<String, TreeNode> entry : node.getChildNodes().entrySet()) {
            printTree2(entry.getKey(), entry.getValue(), spacing + 1);
        }
    }

    private void printTree3(String domainValue, TreeNode node, int spacing) {
        System.out.print("\n");
        for(int i = 0; i<spacing-1; i++) {
            System.out.print("!   ");
        }
        if(spacing > 0)
            System.out.print("!===");
        if(domainValue !=null) {
            System.out.print(  domainValue + " <<<<<");
        }
        System.out.print(node.getData());
        for(Map.Entry<String, TreeNode> entry : node.getChildNodes().entrySet()) {
            printTree3(entry.getKey(), entry.getValue(), spacing + 1);
        }
    }




    public static void main(String arg[]) throws Exception {
        DecisionTree a = new DecisionTree();
        a.execute();

    }

}


class TreeNode {
    private String data;
//    private String data;
    private boolean isPureDataSet;
    private Map<String,TreeNode> childNodes;

    public TreeNode(String data, boolean isPureDataSet) {
        this.data = data;
        this.isPureDataSet = isPureDataSet;
        childNodes = new HashMap<>();
    }

    public String getData() {
        return data;
    }

    public Map<String, TreeNode> getChildNodes() {
        return childNodes;
    }

    @Override
    public String toString() {
        return data +  " "  + isPureDataSet;
    }
}