package com.ideal.main;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title:</p>
 * <p>Description: 余弦获取文章相似性
 * </p>
 *
 * @author xq
 * @version 1.0
 * @createDate：2013-8-26
 */
public class CosineSimilarAlgorithm {

    /**
     * @param @param  firstFile
     * @param @param  secondFile
     * @param @return
     * @return Double
     * @throws
     * @Title: cosSimilarityByFile
     * @Description: 获取两个文件相似性
     */
    public static Double cosSimilarityByFile(String firstFile, String secondFile) {
        try {
            Map<String, Map<String, Integer>> firstTfMap = TfIdfAlgorithm.wordSegCount(firstFile);
            Map<String, Map<String, Integer>> secondTfMap = TfIdfAlgorithm.wordSegCount(secondFile);
            if (firstTfMap == null || firstTfMap.size() == 0) {
                throw new IllegalArgumentException("firstFile not found or firstFile is empty! ");
            }
            if (secondTfMap == null || secondTfMap.size() == 0) {
                throw new IllegalArgumentException("secondFile not found or secondFile is empty! ");
            }
            Map<String, Integer> firstWords = firstTfMap.get(firstFile);
            Map<String, Integer> secondWords = secondTfMap.get(secondFile);
            if (firstWords.size() < secondWords.size()) {
                Map<String, Integer> temp = firstWords;
                firstWords = secondWords;
                secondWords = temp;
            }
            return calculateCos((LinkedHashMap<String, Integer>) firstWords, (LinkedHashMap<String, Integer>) secondWords);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0d;
    }

    /**
     * @param @param  first
     * @param @param  second
     * @param @return
     * @return Double
     * @throws
     * @Title: cosSimilarityByString
     * @Description: 得到两个字符串的相似性
     */
    public static Double cosSimilarityByString(String first, String second) {
        try {
            Map<String, Integer> firstTfMap = TfIdfAlgorithm.segStr(first);
            Map<String, Integer> secondTfMap = TfIdfAlgorithm.segStr(second);
            if (firstTfMap.size() < secondTfMap.size()) {
                Map<String, Integer> temp = firstTfMap;
                firstTfMap = secondTfMap;
                secondTfMap = temp;
            }
            return calculateCos((LinkedHashMap<String, Integer>) firstTfMap, (LinkedHashMap<String, Integer>) secondTfMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0d;
    }

    /**
     * @param @param  first
     * @param @param  second
     * @param @return
     * @return Double
     * @throws
     * @Title: calculateCos
     * @Description: 计算余弦相似性
     */
    private static Double calculateCos(LinkedHashMap<String, Integer> first, LinkedHashMap<String, Integer> second) {

        List<Map.Entry<String, Integer>> firstList = new ArrayList<Map.Entry<String, Integer>>(first.entrySet());
        List<Map.Entry<String, Integer>> secondList = new ArrayList<Map.Entry<String, Integer>>(second.entrySet());
        //计算相似度
        double vectorFirstModulo = 0.00;//向量1的模
        double vectorSecondModulo = 0.00;//向量2的模
        double vectorProduct = 0.00; //向量积
        int secondSize = second.size();
        for (int i = 0; i < firstList.size(); i++) {
            if (i < secondSize) {
                vectorSecondModulo += secondList.get(i).getValue().doubleValue() * secondList.get(i).getValue().doubleValue();
                vectorProduct += firstList.get(i).getValue().doubleValue() * secondList.get(i).getValue().doubleValue();
            }
            vectorFirstModulo += firstList.get(i).getValue().doubleValue() * firstList.get(i).getValue().doubleValue();
        }
        return vectorProduct / (Math.sqrt(vectorFirstModulo) * Math.sqrt(vectorSecondModulo));
    }

    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        File file1 = new File("C:\\Users\\chenyang\\Desktop\\vector1");
        BufferedReader reader1 = null;
        try {
            reader1 = new BufferedReader(new FileReader(file1));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        File file2 = new File("C:\\Users\\chenyang\\Desktop\\vector2");
        BufferedReader reader2 = null;
        String tempString = null;
        String tempString2 = null;
        int i = 0;
        try {
            while ((tempString = reader1.readLine()) != null) {
                reader2 = new BufferedReader(new FileReader(file2));
                while ((tempString2 = reader2.readLine()) != null) {
                    i++;
                    Double result = cosSimilarityByString(tempString,tempString2);
                    System.out.println(tempString+"\t"+tempString2+"\t"+result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(i);
        long end = System.currentTimeMillis();
        System.out.println(end - begin);

    }
}


