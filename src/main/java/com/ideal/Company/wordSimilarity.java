package com.ideal.Company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class wordSimilarity {

	String filename = "content_seg.txt";
	static Map<String, Set<Integer>> admap = new HashMap<String, Set<Integer>>();

	String filename2 = "kehu_seg.txt";
	static Map<String, Set<Integer>> idmap = new HashMap<String, Set<Integer>>();

	static Map<Character, int[]> vectorMap = new HashMap<Character, int[]>();
	static int[] tempArray = null;

	public void load() {
		BufferedReader br = null;
		try {
			InputStream ips = wordSimilarity.class.getClassLoader()
					.getResourceAsStream(filename);
			if (null == ips) {
				ips = wordSimilarity.class.getResourceAsStream(filename);
			}
			br = new BufferedReader(new InputStreamReader(ips, "utf-8"));
			String line = "";
			int j = 1;
			while ((line = br.readLine()) != null) {
				String[] SPLITS = line.trim().split(" ", -1);
				if (SPLITS.length > 0) {
					for (int i = 0; i < SPLITS.length; i++) {
						String tagi = SPLITS[i].trim();
						if (admap.containsKey(tagi)) {
							Set<Integer> numset = admap.get(tagi);
							numset.add(j);
							admap.put(tagi, numset);
						} else {
							Set<Integer> numset = new HashSet<Integer>();
							numset.add(j);
							admap.put(tagi, numset);
						}
					}
				}
				j++;

			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load2() {
		BufferedReader br = null;
		try {
			InputStream ips = wordSimilarity.class.getClassLoader()
					.getResourceAsStream(filename2);
			if (null == ips) {
				ips = wordSimilarity.class.getResourceAsStream(filename2);
			}
			br = new BufferedReader(new InputStreamReader(ips, "utf-8"));
			String line = "";
			int j = 1;
			while ((line = br.readLine()) != null) {
				String[] SPLITS = line.trim().split(" ", -1);
				if (SPLITS.length > 0) {
					for (int i = 0; i < SPLITS.length; i++) {
						String tagi = SPLITS[i].trim();
						if (idmap.containsKey(tagi)) {
							Set<Integer> numset = idmap.get(tagi);
							numset.add(j);
							idmap.put(tagi, numset);
						} else {
							Set<Integer> numset = new HashSet<Integer>();
							numset.add(j);
							idmap.put(tagi, numset);
						}
					}
				}
				j++;

			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Similarity(String string1, String string2) {

		for (Character character1 : string1.toCharArray()) {
			// System.out.println(character1);
			if (vectorMap.containsKey(character1)) {
				vectorMap.get(character1)[0]++;
			} else {
				tempArray = new int[2];
				tempArray[0] = 1;
				tempArray[1] = 0;
				vectorMap.put(character1, tempArray);
			}
			// System.out.println(character1+"\t"+tempArray[0]+"\t"+tempArray[1]);
		}
		for (Character character2 : string2.toCharArray()) {
			// System.out.println(character2);
			if (vectorMap.containsKey(character2)) {
				vectorMap.get(character2)[1]++;
			} else {
				tempArray = new int[2];
				tempArray[0] = 0;
				tempArray[1] = 1;
				vectorMap.put(character2, tempArray);
			}
			// System.out.println(character2+"\t"+tempArray[0]+"\t"+tempArray[1]);
		}
	}

	// 求余弦相似度
	public double sim() {
		double result = 0;
		result = pointMulti(vectorMap) / sqrtMulti(vectorMap);
		// System.out.println(pointMulti(vectorMap)+"\t"+squares(vectorMap));
		return result;
	}

	private double sqrtMulti(Map<Character, int[]> paramMap) {
		double result = 0;
		result = squares(paramMap);
		result = Math.sqrt(result);
		return result;
	}

	// 求平方和
	private double squares(Map<Character, int[]> paramMap) {
		double result1 = 0;
		double result2 = 0;
		Set<Character> keySet = paramMap.keySet();
		for (Character character : keySet) {
			int temp[] = paramMap.get(character);
			result1 += (temp[0] * temp[0]);
			result2 += (temp[1] * temp[1]);
		}
		return result1 * result2;
	}

	// 点乘法
	private double pointMulti(Map<Character, int[]> paramMap) {
		double result = 0;
		Set<Character> keySet = paramMap.keySet();
		for (Character character : keySet) {
			int temp[] = paramMap.get(character);
			// System.out.println(temp[0]);
			// System.out.println(temp[1]);
			result += (temp[0] * temp[1]);
		}
		return result;
	}

	public static void main(String[] args) throws IOException {

		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
				"E://data//word_similarity.txt")));
		wordSimilarity s = new wordSimilarity();
		s.load();
		s.load2();

		/*
		 * for (String key : admap.keySet()) { int size = admap.get(key).size();
		 * System.out.println(key + "\t"+ size + "\t"+ admap.get(key) );
		 * bw.write(key + "\t"+ size + "\t"+ admap.get(key) + "\r\n");
		 * bw.flush(); }
		 */

		
		/*for(String str_id: admap.keySet()){
			bw.write(str_id + "\t"+ admap.get(str_id).size() + "\r\n");
			bw.flush();
		}*/
		
	
		
		for (String str_id : idmap.keySet()) {
			for (String str_ad : admap.keySet()) {

				s.Similarity(str_id, str_ad);
				Double d = s.sim();
				if (d > 0.95) {
					System.out.println(str_id + "\t" + str_ad);
					System.out.println(d);
					bw.write(d + "\t" + str_id + "\t" + idmap.get(str_id)
							+ "\t" + str_ad + "\t" + admap.get(str_ad) + "\r\n");
							
					bw.flush();

				}
				vectorMap = new HashMap<Character, int[]>();
				tempArray = null;
			}

		}

		// 检测结果
		/*
		 * for (int key : idmap.keySet()) { System.out.println(key);
		 * System.out.println(idmap.get(key)); }
		 */
	}
}