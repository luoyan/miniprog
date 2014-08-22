package com.luoyan.syntax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.xiaomi.miui.ad.thrift.model.Constants;

public class GetLowCorrelationPosition {
    private static void usage() {
        System.out.println("command ctr_file query_file");
    }

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
    
    private static long getMaxCtrAfterAd(String[] ctr_info_list, int start) {
        long max = 0;
        for (int i = start + 1; i < ctr_info_list.length; i++) {
            String ctr_info = ctr_info_list[i];
            long ctr = Long.parseLong(ctr_info.split(":")[1]);
            if (ctr > max)
                max = ctr;
        }
        return max;
    }
    private static Map<String, Integer> getAdPosition(String fileName, Set<String> querySet, int threshold, int strategy) throws IOException {
        int count = 0;
        String line = null;
        int ctr_lt_4 = 0;//no need to change
        int ctr_gt_4 = 0;//need to change
        int ctr_eq_4 = 0;//need to change
        int total = 0;
        long ctr_sum = 0;
        long prev_ctr_sum = 0;
        long maxCtrAfterAdSum = 0;
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        Map<String, Integer> positionMap = new HashMap<String, Integer>();
        while ((line = br.readLine()) != null) {
            try {
                String[] terms = line.split(Constants.LOG_SEPARATOR);
                
                if (2 == terms.length && querySet.contains(terms[0])) {
                    total ++;
                    String query = terms[0];
                    String[] ctr_info_list = terms[1].split(",");
                    long prev_ctr = -1;
                    for (int i = 0; i < ctr_info_list.length ; i++) {
                        String ctr_info = ctr_info_list[i];
                        long appId = Long.parseLong(ctr_info.split(":")[0]);
                        long ctr = Long.parseLong(ctr_info.split(":")[1]);
                        //System.out.println("Query [" + query + "] " + i + " appId " + appId + " ctr " + ctr);
                        boolean reachCriticalPoint = false;
                        if (strategy == 0)
                            reachCriticalPoint = (i > 0 && prev_ctr < ctr && prev_ctr < threshold);
                        else if (strategy == 1)
                            reachCriticalPoint = (i > 0 && prev_ctr < threshold);
                        if (reachCriticalPoint) {
                            ctr_sum += ctr;
                            prev_ctr_sum += prev_ctr;
                            int low = i - 1;
                            //System.out.println(line);
                            //System.out.println("Query [" + query + "] " + low + " appId " + appId + " low ctr " + ctr + " prev_ctr " + prev_ctr);
                            if (low < 3)
                                ctr_lt_4++;
                            else if (low == 3)
                                ctr_eq_4++;
                            else
                                ctr_gt_4++;
                            positionMap.put(query, i);
                            long maxCtrAfterAd = getMaxCtrAfterAd(ctr_info_list, i);
                            maxCtrAfterAdSum += maxCtrAfterAd;
                            break;
                        }
                        prev_ctr = ctr;
                    }
                }
                else if (querySet.contains(terms[0])) {
                    //System.err.println("terms != 2 [" + line + "]");
                }

                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("threshold " + threshold + " strategy " + strategy + " total " + total + " first min ctr avg " + prev_ctr_sum/total + " max ctr after ad avg " + maxCtrAfterAdSum/total);
        return positionMap;
    }
    private static void getStatistics(Map<String, Integer> positionMap) {
        
        Iterator iter = positionMap.entrySet().iterator();
        int[] scale = new int[8];
        int total = 0;
        while (iter.hasNext()) { 
            Map.Entry entry = (Map.Entry) iter.next(); 
            Object key = entry.getKey(); 
            int val = (Integer)entry.getValue();
            if (val < 7) {
                scale[val] ++;
            }
            else
                scale[7]++;
            total++;
        } 
        String info = "";
        /*
        for (int i = 0;i < 7;i++) {
            int rate = scale[i] * 100 / total;
            info += ((i+1) + " : " + rate + "% ");
        }*/
        int rate = (scale[0] + scale[1] + scale[2]) * 100 / total;
        info += (" (< 4)" + " : " + rate + "% ");
        rate = (scale[3] + scale[4] + scale[5]) * 100 / total;
        info += (" (4 ~ 6)" + " : " + rate + "% ");
        rate = (scale[6]) * 100 / total;
        info += (" (7)" + " : " + rate + "% ");
        rate = scale[7] * 100 / total;
        info += (" (> 7)" + " : " + rate + "% ");
        System.out.println(info);
    }
    
    private static void dumpStatistics(Map<String, Integer> positionMap, String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = null;
        while ((line = br.readLine()) != null) {
            String query = line.trim();
            Integer val = positionMap.get(query);
            if (val != null) {
                int pos = val + 1;
                System.out.println(query + "\t" + pos);
            }
            else {
                System.out.println(query + "\t");
            }
        }
    }
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            usage();
            return ;
        }
        String fileName = args[0];
        String queryFile = args[1];
        Set<String> querySet = loadQueryFile(queryFile);
        //GetStatistics(getAdPosition(fileName, querySet, 3 * 100, 0));
        dumpStatistics(getAdPosition(fileName, querySet, 5 * 100, 1), queryFile);
        /*
        for (int s = 0; s < 2; s++) {
            for (int i = 3 ; i < 7 ; i++) { 
                getStatistics(getAdPosition(fileName, querySet, i * 100, s));
            }
        }*/
    }
}
