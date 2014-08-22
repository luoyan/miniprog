package com.luoyan.syntax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class getInaccuracyQuery {

	private static Set<String> loadQueryFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = null;
        Set<String> set = new TreeSet<String>();
        while ((line = br.readLine()) != null) {
            String query = line.trim();
            set.add(query);
        }
        return set;
	}
	private static Map<String, List<Long>> loadCtrFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = null;
        Map<String, List<Long>> ctrMap = new HashMap<String, List<Long>>();
        while ((line = br.readLine()) != null) {
        	String[] items = line.trim().split("\t");  
            String query = items[0];
            if (items.length == 2) {
                String[] ctrInfoArray = items[1].split(",");
                //Map<Long, Long> ctrInfoMap = new HashMap<Long, Long>();
                List<Long> ctrInfoList = new ArrayList<Long>();
                for (String ctrInfo : ctrInfoArray) {
                	long appId = Long.parseLong(ctrInfo.split(":")[0]);
                	long ctr = Long.parseLong(ctrInfo.split(":")[1]);
                	ctrInfoList.add(ctr);
                }
                ctrMap.put(query, ctrInfoList);
            }
        }
        return ctrMap;
	}
	
	private static List<String> getInaccuracyQuery(String queryFileName, String ctrFileName, long ctrThreshold) throws IOException {
		Set<String> querySet = loadQueryFile(queryFileName);
		Map<String, List<Long>> ctrMap = loadCtrFile(ctrFileName);
		List<String> inaccuracyQueryList = new ArrayList<String>();
		int count = 0;
		for (String query : querySet) {
			List<Long> ctrInfoList = ctrMap.get(query);
			if (ctrInfoList != null) {
				if (ctrInfoList.size() > 0) {
					long firstCtr = ctrInfoList.get(0);
					if (firstCtr < ctrThreshold) {
						inaccuracyQueryList.add(query);
						count++;
						System.out.println(query + "\t" + firstCtr);
					}
				}
			}
			else {
				System.out.println(query + "\tnotfound");
			}
		}
		System.out.println("total " + count);
		return inaccuracyQueryList;
	}
	public static void usage() {
		System.err.println("command queryFile ctrFile threshold");
	}
	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			usage();
			System.exit(-1);
		}
		String queryFile = args[0];
		String ctrFile = args[1];
		long threshold = Long.parseLong(args[2]);
		List<String> inaccuracyQueryList = getInaccuracyQuery(queryFile, ctrFile, threshold);
	}
}
