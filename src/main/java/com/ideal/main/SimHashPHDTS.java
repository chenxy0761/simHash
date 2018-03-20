package com.ideal.main;

import org.apdplat.word.analysis.CosineTextSimilarity;
import org.apdplat.word.analysis.TextSimilarity;

public class SimHashPHDTS {
    public static void main(String[] args) {
        String text2 = "我爱读书";
        String text1 = "他是黑客";
        TextSimilarity textSimilarity = new CosineTextSimilarity();
        double score1pk1 = textSimilarity.similarScore(text1, text1);
        double score1pk2 = textSimilarity.similarScore(text1, text2);
        System.out.println(text1+" 和 "+text1+" 的相似度分值："+score1pk1);
        System.out.println(text1+" 和 "+text2+" 的相似度分值："+score1pk2);
    }
}
