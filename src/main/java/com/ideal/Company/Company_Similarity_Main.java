package com.ideal.Company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Company_Similarity_Main {
	public static void PrintWordCnt(String filename, String output) {
		Map<String, Integer> WordCnt = new HashMap<String, Integer>();

		BufferedWriter bw = null;
		BufferedReader br = null;
		try {
			InputStream ips = wordSimilarity.class.getClassLoader()
					.getResourceAsStream(filename);
			if (null == ips) {
				ips = wordSimilarity.class.getResourceAsStream(filename);
			}
			bw = new BufferedWriter(new FileWriter(new File(output), true));
			br = new BufferedReader(new InputStreamReader(ips, "utf-8"));
			String line = "";
			int j = 1;
			while ((line = br.readLine()) != null) {
				String[] SPLITS = line.trim().split(" ", -1);
				if (SPLITS.length > 0) {
					for (int i = 0; i < SPLITS.length; i++) {
						String tagi = SPLITS[i].trim();
						if (WordCnt.containsKey(tagi)) {
							int cnt = WordCnt.get(tagi);
							cnt++;
							WordCnt.put(tagi, cnt);
						} else {
							WordCnt.put(tagi, 1);
						}
					}
				}
				j++;

			}

			for (String key : WordCnt.keySet()) {
				int cnt = WordCnt.get(key);
				bw.write(key + "\t" + cnt + "\n");
				bw.flush();
//				System.out.println(key + "\t" + cnt + "\n");
			}
			br.close();
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static Map<String, List<String>> load(String filename) {
		Map<String, List<String>> keywordsCompany = new HashMap<String, List<String>>();
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
				String companyName = line.trim();
				String[] SPLITS = line.trim().split(" ", -1);
				if (SPLITS.length > 0) {
					for (int i = 0; i < SPLITS.length; i++) {
						String tagi = SPLITS[i].trim();
						if (keywordsCompany.containsKey(tagi)) {
							List<String> numset = keywordsCompany.get(tagi);
							numset.add(companyName);
							keywordsCompany.put(tagi, numset);
						} else {
							List<String> numset = new ArrayList<String>();
							numset.add(companyName);
							keywordsCompany.put(tagi, numset);
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
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return keywordsCompany;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
				"E://data//telecom_tjcom.txt")));
		Map<String, List<String>> TeleCompany = load("content_seg.txt");
		Map<String, List<String>> TongjiCompany = load("kehu_seg.txt");
		Map<String, List<String>> all = load("all.txt");
		PrintWordCnt("content_seg.txt", "content_wordcnt.txt");
		PrintWordCnt("kehu_seg.txt", "kehu_wordcnt.txt");
		PrintWordCnt("all.txt", "all_wordcnt.txt");
		for (String key : TongjiCompany.keySet()) {
			if (TeleCompany.containsKey(key)) {
				List<String> TeleCompany_list = TeleCompany.get(key);
				List<String> TJCompany_list = TongjiCompany.get(key);
				int TelCom_Cnt = TeleCompany_list.size();
				int TJCom_Cnt = TJCompany_list.size();
				if (TJCom_Cnt < 100) {
					for (String telCom : TeleCompany_list) {
						for (String TJCom : TJCompany_list) {
							// bw.write(telCom+"\t"+TJCom + "\r\n");
							// bw.flush();
							String[] telList = telCom.split("\\s+");
							String[] tjlist = TJCom.split("\\s+");
							// 计算交集 词的数量
							// 计算在A中，不在B中词的数量
							// 计算在B中，不在A中词的数量
							// 计算整个公司名称中，每个词的频率。
							List<String> tele = Intersection(telList, tjlist);
							List<String> A = Diffset(telList, tjlist);
							List<String> B = Diffset(tjlist, telList);
							System.out.println(telCom + "||" + telList.length + "||"
									+ TJCom + "||" + tjlist.length + "||"
									+ tele + "||" + tele.size() + "||" + A
									+ "||" + A.size() + "||" + B + "||"
									+ B.size());
							bw.write(telCom + "|" + telList.length + "|"
									+ TJCom + "|" + tjlist.length + "|"
									+ tele + "|" + tele.size() + "|" + A
									+ "|" + A.size() + "|" + B + "|"
									+ B.size() + "\r\n");
							bw.flush();

						}
					}
				}
			}
		}
	}

	// 计算交集 词的数量
	public static ArrayList<String> Intersection(String[] tele, String[] tj) {
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		for (String i : tele)
			list1.add(i);
		for (String i : tj)
			list2.add(i);
		list1.retainAll(list2);
		return list1;
	}

	// 并集 词的数量
	public static ArrayList<String> Union(String[] tele, String[] tj) {
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		for (String i : tele)
			list1.add(i);
		for (String i : tj)
			list2.add(i);
		list1.removeAll(list2);
		list2.addAll(list1);
		return list2;
	}

	// 计算在A中，不在B中词的数量
	public static ArrayList<String> Diffset(String[] listA, String[] listB) {

		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		for (String i : listA)
			list1.add(i);
		for (String i : listB)
			list2.add(i);

		list1.removeAll(list2);
		return list1;
	}
}
