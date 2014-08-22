package com.luoyan.syntax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiaomi.appstore.backend.model.AppData;
import com.xiaomi.appstore.backend.model.AppInfo;
import com.xiaomi.localclient.LocalClientFactory;
import com.xiaomi.marketing.exception.CatchableException;
import com.xiaomi.miliao.thrift.ClientFactory;
import com.xiaomi.miliao.zookeeper.EnvironmentType;
import com.xiaomi.miliao.zookeeper.ZKFacade;
import com.xiaomi.miui.ad.thrift.model.AppStat;
import com.xiaomi.miui.ad.thrift.model.BiddingInfo;
import com.xiaomi.miui.ad.thrift.model.BiddingStatus;
import com.xiaomi.miui.ad.thrift.model.Constants;
import com.xiaomi.miui.ad.thrift.model.CtrScore;
import com.xiaomi.miui.ad.thrift.service.MiuiAdStoreService;
import com.xiaomi.miui.analytics.util.factory.RoseBeanFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Syntax {
    private static MiuiAdStoreService.Iface miuiAdStoreServiceClient = ClientFactory.getClient(MiuiAdStoreService.Iface.class, 100000);

    @Autowired
    private AppStoreBackendServiceProxy appStoreBackendServiceProxy;

	private void getZookeeperList(String environment) {
        System.out.println(environment + " zookeeper list : [" + ZKFacade.getZKSettings().getZKServers() + "]");
	}
	private void getBiddingInfoList() throws TException {
		List<BiddingInfo> biddingInfos = miuiAdStoreServiceClient.getBiddingInfoByStatus(
                BiddingStatus.NORMAL, 0, 100000);
		int count = 0;
		for (BiddingInfo biddingInfo : biddingInfos) {
			System.out.println(count + " : " + biddingInfo.getName() + "\t" + biddingInfo.getAppId());
			count ++;
		}
	}
	private void getAllPositionTypes() throws TException {
		Set<String> types = miuiAdStoreServiceClient.getAllPositionTypes();
		for (String category : types) {
			System.out.println("category [" + category + "]");
			Set<String> features = miuiAdStoreServiceClient.getManualRecommendFeaturedList(category);
			for (String feature : features) {
				System.out.println("feature : " + feature);
			}
		}
	}
	private void getAppList() throws TException, CatchableException {
		int offset = 0;
		int batchNum = 1000;
		int count = 0;
		appStoreBackendServiceProxy = new AppStoreBackendServiceProxy();
		int batchResultCount = batchNum;
		while (batchResultCount == batchNum) {
            List<AppData> appDataList = appStoreBackendServiceProxy.getAppList(offset, batchNum);
            batchResultCount = appDataList.size();
            offset += batchNum;
            //System.out.println("offset " + offset);
            for (AppData appData : appDataList) {
            	System.out.println(count + " : " + appData.getAppInfo().getPackageName() + "\t" + appData.getAppInfo().getAppId() + "\t" + appData.getAppInfo().getDisplayName());
            	count ++;
            }
		}
	}
	
    private void getAppDownloadNum() throws IOException, TException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    String s;
	    while ((s = in.readLine()) != null && s.length() != 0) {
	    	List<String> packageNames = new ArrayList<String>();
	    	packageNames.add(s);
	    	List<AppStat> appStatList = miuiAdStoreServiceClient.getAppStatListByPackageNames(packageNames);
	    	//System.out.println("appStatList.size() " + appStatList.size());
	    	for (AppStat appStat : appStatList) {
	    	    System.out.println("downloadNum " + appStat.getAllDownloadNumTotal());
	    	}
	    }
    }
    
    private void getManualRecommendFeatureList() {
        try {
            Set<String> positionTypes = miuiAdStoreServiceClient.getAllPositionTypes();
            System.out.println("Current position types " + positionTypes);
            if (positionTypes.size() > 0) {
                //Map<String, Set<String>> currentManualRecommendFeaturedListMap = new HashMap<String, Set<String>>();
                
                for (String item : positionTypes) {
                	Set<String> set = miuiAdStoreServiceClient.getManualRecommendFeaturedList(item);
                	String info = "";
                	for (String feature : set) {
                		info = info + "," + feature;
                	}
                	System.out.println("positionType : [" + item + "] feature [" + info + "]");
                    //currentManualRecommendFeaturedListMap.put(item, set);
                }
                //System.out.println("Current manual recommend featured list map " + currentManualRecommendFeaturedListMap);
            }
        } catch (Exception e) {
        	System.err.println("Failed to update manual recommend featured lists." + e);
        }
    }
    
    private void getAllCtrScores(String algorithmName) throws TException {
    	System.out.println("zookeeper list : [" + ZKFacade.getZKSettings().getZKServers() + "]");
        List<CtrScore> ctrScores = miuiAdStoreServiceClient.getAllCtrScores(algorithmName);
        System.out.println("ctrScores " + ctrScores.size());
        for (CtrScore ctrScore : ctrScores) {
            System.out.println("packageName " + ctrScore.getPackageName() + " ctr " + ctrScore.getScore() + " algorithm " + ctrScore.getAlgorithmName());
        }
    }
	
	public static void testGetKeywordList() throws TException {
        String keywordStr = miuiAdStoreServiceClient.getString("ad:11:", "1");
        List<String> keywordList = Utils.getKeywordListFromBase64Content(keywordStr);
        System.out.println("keywordStr : " + keywordStr);
        int n = 0;
        for (String keyword : keywordList) {
        //	System.out.println(n + " keyword : " + keyword);
        	n += 1;
        }
        String recommendAdStr = miuiAdStoreServiceClient.getString("ad:9", "�ε�");
        System.out.println("recommendAdStr " + recommendAdStr);
	}
	
	private static void usage() {
		System.err.println("args envirenment=[onebox/staging/shangdi] command=[getZookeeperList/getBiddingInfoList/getAppList/getAllPositionTypes/getManualRecommendFeatureList/getAllCtrScores [algrithm]]");
		System.err.println("algorithm = ma/recommend/simple/decay/random/nma/lrctr/purelrctr");
	}
	
	public static void main(String[] args) throws TException, CatchableException, IOException {
		Syntax s = new Syntax();
		if (args.length < 2) {
			usage();
			System.exit(-1);
		}
		String environment = args[0];
		String command = args[1];
		if (!Utils.setEnvironment(environment)) {
			usage();
			System.exit(-1);
		}
		
		if (command.equals("getZookeeperList")) {
			s.getZookeeperList(environment);
		}
		else if (command.equals("getBiddingInfoList")) {
			s.getBiddingInfoList();
		}
		else if (command.equals("getAppList")) {
			s.getAppList();
		}
		else if (command.equals("getAppDownloadNum")) {
			s.getAppDownloadNum();
		}
		else if (command.equals("getAllPositionTypes")) {
			s.getAllPositionTypes();
		}
		else if (command.equals("getManualRecommendFeatureList")) {
			s.getManualRecommendFeatureList();
		}
		else if (command.equals("getAllCtrScores") && args.length == 3) {
			String algorithmName = args[2];
			s.getAllCtrScores(algorithmName);
		}
		else {
			usage();
			System.exit(-1);
		}
	}

}
